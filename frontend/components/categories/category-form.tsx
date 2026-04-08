"use client";

import { useEffect } from "react";
import { Modal, Form, Input, Select, Radio, Button, message } from "antd";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { categoryApi } from "@/lib/api/categories";
import { useAuth } from "@/components/providers/auth-provider";
import { CategoryResponse } from "@/lib/types";

interface CategoryFormProps {
  open: boolean;
  onClose: () => void;
  category?: CategoryResponse | null;
}

export function CategoryForm({ open, onClose, category }: CategoryFormProps) {
  const [form] = Form.useForm();
  const { user } = useAuth();
  const queryClient = useQueryClient();

  useEffect(() => {
    if (open && category) {
      form.setFieldsValue({
        name: category.name,
        type: category.type,
      });
    } else if (open) {
      form.resetFields();
      form.setFieldsValue({ type: "EXPENSE" });
    }
  }, [open, category, form]);

  const createMutation = useMutation({
    mutationFn: categoryApi.create,
    onSuccess: () => {
      message.success("Categoria criada com sucesso!");
      queryClient.invalidateQueries({ queryKey: ["categories"] });
      onClose();
    },
    onError: (error: any) => {
      message.error(error.response?.data?.error || "Erro ao criar categoria.");
    }
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: string, data: any }) => categoryApi.update(id, data),
    onSuccess: () => {
      message.success("Categoria atualizada!");
      queryClient.invalidateQueries({ queryKey: ["categories"] });
      onClose();
    },
    onError: (error: any) => {
      message.error(error.response?.data?.error || "Erro ao atualizar.");
    }
  });

  const onFinish = (values: any) => {
    if (category) {
      updateMutation.mutate({
        id: category.id,
        data: {
          name: values.name,
          type: category.type, // Backend update doesn't usually change type, but we send it if needed
        }
      });
    } else {
      createMutation.mutate({
        userId: user?.id as string,
        name: values.name,
        type: values.type,
      });
    }
  };

  return (
    <Modal
      title={category ? "Editar Categoria" : "Nova Categoria"}
      open={open}
      onCancel={onClose}
      footer={null}
      destroyOnClose
    >
      <Form
        form={form}
        layout="vertical"
        onFinish={onFinish}
        style={{ marginTop: 24 }}
      >
        <Form.Item
          name="name"
          label="Nome"
          rules={[{ required: true, message: "Insira o nome da categoria" }]}
        >
          <Input size="large" />
        </Form.Item>

        {!category && (
          <Form.Item name="type" label="Tipo">
            <Radio.Group buttonStyle="solid" style={{ display: "flex", width: "100%" }}>
              <Radio.Button value="EXPENSE" style={{ flex: 1, textAlign: "center" }}>Despesa</Radio.Button>
              <Radio.Button value="INCOME" style={{ flex: 1, textAlign: "center" }}>Receita</Radio.Button>
            </Radio.Group>
          </Form.Item>
        )}

        <Form.Item style={{ marginBottom: 0, marginTop: 32 }}>
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
    </Modal>
  );
}
