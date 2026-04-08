"use client";

import { useEffect } from "react";
import { Drawer, Form, Input, Button, DatePicker, Select, Radio, message, InputNumber, Alert, Checkbox } from "antd";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import dayjs from "dayjs";
import { transactionApi } from "@/lib/api/transactions";
import { categoryApi } from "@/lib/api/categories";
import { useAuth } from "@/components/providers/auth-provider";
import { TransactionResponse, CreateTransactionCommand, UpdateTransactionCommand } from "@/lib/types";

interface TransactionFormProps {
  open: boolean;
  onClose: () => void;
  transaction?: TransactionResponse | null;
}

export function TransactionForm({ open, onClose, transaction }: TransactionFormProps) {
  const [form] = Form.useForm();
  const { user } = useAuth();
  const queryClient = useQueryClient();
  const [messageApi, contextHolder] = message.useMessage();

  const isExpense = Form.useWatch("type", form) === "EXPENSE";

  useEffect(() => {
    if (open && transaction) {
      form.setFieldsValue({
        description: transaction.description,
        amount: transaction.amount,
        date: dayjs(transaction.date),
        categoryId: transaction.categoryId,
        type: transaction.type,
        status: transaction.status,
      });
    } else if (open) {
      form.resetFields();
      form.setFieldsValue({
        type: "EXPENSE",
        status: "PENDING",
        date: dayjs(),
      });
    }
  }, [open, transaction, form]);

  const { data: categories, isLoading: loadingCategories } = useQuery({
    queryKey: ["categories", user?.id],
    queryFn: () => categoryApi.findAllByUserId(user?.id as string),
    enabled: !!user?.id && open,
  });

  const createMutation = useMutation({
    mutationFn: transactionApi.create,
    onSuccess: () => {
      messageApi.success("Transação criada com sucesso!");
      queryClient.invalidateQueries({ queryKey: ["transactions"] });
      queryClient.invalidateQueries({ queryKey: ["summary"] });
      onClose();
    },
    onError: (error: any) => {
      messageApi.error(error.response?.data?.error || "Erro ao criar transação.");
    }
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: string, data: UpdateTransactionCommand }) => transactionApi.update(id, data),
    onSuccess: () => {
      messageApi.success("Transação atualizada!");
      queryClient.invalidateQueries({ queryKey: ["transactions"] });
      queryClient.invalidateQueries({ queryKey: ["summary"] });
      onClose();
    },
    onError: (error: any) => {
      messageApi.error(error.response?.data?.error || "Erro ao atualizar.");
    }
  });

  const onFinish = (values: any) => {
    const formattedDate = values.date.format("YYYY-MM-DD");

    if (transaction) {
      updateMutation.mutate({
        id: transaction.id,
        data: {
          description: values.description,
          amount: values.amount,
          date: formattedDate,
          status: values.status,
          categoryId: values.categoryId,
        }
      });
    } else {
      createMutation.mutate({
        userId: user?.id as string,
        categoryId: values.categoryId,
        description: values.description,
        amount: values.amount,
        date: formattedDate,
        status: values.status,
        type: values.type,
        recurringTemplateId: undefined, // Ignorado por enquanto
      });
    }
  };

  const filteredCategories = categories?.filter(c => c.type === (isExpense ? "EXPENSE" : "INCOME")) || [];

  return (
    <>
      {contextHolder}
      <Drawer
        title={transaction ? "Editar Transação" : "Nova Transação"}
        placement="bottom"
        height="auto"
        open={open}
        onClose={onClose}
        style={{ borderTopLeftRadius: 16, borderTopRightRadius: 16, maxWidth: 640, margin: "0 auto" }}
        destroyOnClose
      >
        <Form
          form={form}
          layout="vertical"
          onFinish={onFinish}
          initialValues={{ type: "EXPENSE", status: "PENDING", date: dayjs() }}
        >
          {!transaction && (
            <Form.Item name="type" label="Tipo">
              <Radio.Group buttonStyle="solid" style={{ display: "flex", width: "100%" }}>
                <Radio.Button value="EXPENSE" style={{ flex: 1, textAlign: "center" }}>Despesa</Radio.Button>
                <Radio.Button value="INCOME" style={{ flex: 1, textAlign: "center" }}>Receita</Radio.Button>
              </Radio.Group>
            </Form.Item>
          )}

          <Form.Item
            name="amount"
            label="Valor"
            rules={[{ required: true, message: "Insira o valor" }]}
          >
            <InputNumber
              prefix="R$"
              style={{ width: "100%" }}
              size="large"
              min={0.01}
              step={0.01}
              precision={2}
              decimalSeparator=","
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
              showSearch
              filterOption={(input, option) =>
                (option?.label ?? '').toString().toLowerCase().includes(input.toLowerCase())
              }
              options={filteredCategories.map(c => ({ value: c.id, label: c.name }))}
            />
          </Form.Item>

          <div style={{ display: "flex", gap: 16 }}>
            <Form.Item
              name="date"
              label="Data"
              style={{ flex: 1 }}
              rules={[{ required: true, message: "Obrigatório" }]}
            >
              <DatePicker size="large" style={{ width: "100%" }} format="DD/MM/YYYY" />
            </Form.Item>

            <Form.Item
              name="status"
              label="Status"
              style={{ flex: 1 }}
              rules={[{ required: true, message: "Obrigatório" }]}
            >
              <Select size="large">
                <Select.Option value="PENDING">Pendente</Select.Option>
                <Select.Option value="PAID">Pago</Select.Option>
                {isExpense === false && <Select.Option value="INVESTED">Investido</Select.Option>}
                <Select.Option value="REFUNDED">Reembolsado</Select.Option>
                <Select.Option value="CANCELLED">Cancelado</Select.Option>
              </Select>
            </Form.Item>
          </div>

          <Alert 
            message="Para transações recorrentes e parceladas, use a aba Recorrentes." 
            type="info" 
            showIcon 
            style={{ marginBottom: 24 }}
          />

          <Form.Item>
            <Button 
              type="primary" 
              htmlType="submit" 
              block 
              size="large" 
              loading={createMutation.isPending || updateMutation.isPending}
            >
              Salvar
            </Button>
          </Form.Item>
        </Form>
      </Drawer>
    </>
  );
}
