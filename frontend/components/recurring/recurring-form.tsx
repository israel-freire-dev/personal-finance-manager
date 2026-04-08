"use client";

import { useEffect, useState } from "react";
import { Drawer, Form, Input, Select, Button, DatePicker, message, InputNumber, Radio, Checkbox } from "antd";
import { useMutation, useQueryClient, useQuery } from "@tanstack/react-query";
import dayjs from "dayjs";
import { recurringApi } from "@/lib/api/recurring";
import { categoryApi } from "@/lib/api/categories";
import { useAuth } from "@/components/providers/auth-provider";
import { CreateRecurringTemplateCommand } from "@/lib/types";

interface RecurringFormProps {
  open: boolean;
  onClose: () => void;
}

export function RecurringForm({ open, onClose }: RecurringFormProps) {
  const [form] = Form.useForm();
  const { user } = useAuth();
  const queryClient = useQueryClient();
  const [isInstallment, setIsInstallment] = useState(false);

  const { data: categories, isLoading: loadingCategories } = useQuery({
    queryKey: ["categories", user?.id],
    queryFn: () => categoryApi.findAllByUserId(user?.id as string),
    enabled: !!user?.id && open,
  });

  useEffect(() => {
    if (open) {
      form.resetFields();
      form.setFieldsValue({
        type: "EXPENSE",
        frequency: "MONTHLY",
        startDate: dayjs(),
      });
      setIsInstallment(false);
    }
  }, [open, form]);

  const createMutation = useMutation({
    mutationFn: recurringApi.create,
    onSuccess: () => {
      message.success("Lançamento recorrente criado com sucesso!");
      queryClient.invalidateQueries({ queryKey: ["recurring"] });
      onClose();
    },
    onError: (error: any) => {
      message.error(error.response?.data?.error || "Erro ao criar lançamento recorrente.");
    }
  });

  const onFinish = (values: any) => {
    const command: CreateRecurringTemplateCommand = {
      userId: user?.id as string,
      categoryId: values.categoryId,
      description: values.description,
      amount: values.amount,
      frequency: values.frequency,
      startDate: values.startDate.format("YYYY-MM-DD"),
      endDate: isInstallment && values.totalInstallments ? undefined : undefined, 
      totalInstallments: isInstallment ? values.totalInstallments : undefined,
    };

    createMutation.mutate(command);
  };

  const isExpense = Form.useWatch("type", form) === "EXPENSE";
  const filteredCategories = categories?.filter(c => c.type === (isExpense ? "EXPENSE" : "INCOME")) || [];

  return (
    <Drawer
      title="Novo Lançamento Recorrente"
      placement="right"
      width={400}
      open={open}
      onClose={onClose}
      destroyOnClose
    >
      <Form
        form={form}
        layout="vertical"
        onFinish={onFinish}
        initialValues={{ type: "EXPENSE", frequency: "MONTHLY", startDate: dayjs() }}
      >
        <Form.Item name="type" label="Tipo">
          <Radio.Group buttonStyle="solid" style={{ display: "flex", width: "100%" }}>
            <Radio.Button value="EXPENSE" style={{ flex: 1, textAlign: "center" }}>Despesa</Radio.Button>
            <Radio.Button value="INCOME" style={{ flex: 1, textAlign: "center" }}>Receita</Radio.Button>
          </Radio.Group>
        </Form.Item>

        <Form.Item
          name="amount"
          label={isInstallment ? "Valor da Parcela" : "Valor Fixo"}
          rules={[{ required: true, message: "Insira o valor" }]}
        >
          <InputNumber
            prefix="R$"
            style={{ width: "100%" }}
            size="large"
            min={0.01}
            step={0.01}
            precision={2}
          />
        </Form.Item>

        <Form.Item
          name="description"
          label="Descrição"
          rules={[{ required: true, message: "Insira uma descrição" }]}
        >
          <Input size="large" />
        </Form.Item>

        <Form.Item
          name="categoryId"
          label="Categoria"
          rules={[{ required: true, message: "Selecione uma categoria" }]}
        >
          <Select 
            size="large" 
            loading={loadingCategories}
            options={filteredCategories.map(c => ({ value: c.id, label: c.name }))}
          />
        </Form.Item>

        <div style={{ display: "flex", gap: 16 }}>
          <Form.Item
            name="startDate"
            label="Mês/Data Base"
            style={{ flex: 1 }}
            rules={[{ required: true, message: "Obrigatório" }]}
          >
            <DatePicker size="large" style={{ width: "100%" }} format="DD/MM/YYYY" />
          </Form.Item>

          <Form.Item
            name="frequency"
            label="Frequência"
            style={{ flex: 1 }}
            rules={[{ required: true, message: "Obrigatório" }]}
          >
            <Select size="large">
              <Select.Option value="MONTHLY">Mensal</Select.Option>
              <Select.Option value="WEEKLY">Semanal</Select.Option>
              <Select.Option value="ANNUAL">Anual</Select.Option>
            </Select>
          </Form.Item>
        </div>

        <div style={{ marginBottom: 24 }}>
          <Checkbox 
            checked={isInstallment} 
            onChange={(e) => setIsInstallment(e.target.checked)}
          >
            É uma compra parcelada / tem fim determinado?
          </Checkbox>
        </div>

        {isInstallment && (
          <Form.Item
            name="totalInstallments"
            label="Número de Parcelas"
            rules={[{ required: true, message: "Insira o número de parcelas" }]}
            style={{ animation: "fadeIn 0.3s" }}
          >
            <InputNumber 
              min={2} 
              max={360} 
              size="large" 
              style={{ width: "100%" }} 
            />
          </Form.Item>
        )}

        <Form.Item>
          <Button 
            type="primary" 
            htmlType="submit" 
            block 
            size="large" 
            loading={createMutation.isPending}
          >
            Criar Lançamento Recorrente
          </Button>
        </Form.Item>
      </Form>
    </Drawer>
  );
}
