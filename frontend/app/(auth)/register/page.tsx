"use client";

import { Card, Form, Input, Button, message } from "antd";
import { UserOutlined, LockOutlined, MailOutlined } from "@ant-design/icons";
import Link from "next/link";
import { useMutation } from "@tanstack/react-query";
import { authApi } from "@/lib/api/auth";
import { RegisterUserCommand } from "@/lib/types";
import { useAuth } from "@/components/providers/auth-provider";

export default function RegisterPage() {
  const [form] = Form.useForm<RegisterUserCommand>();
  const { login } = useAuth();
  const [messageApi, contextHolder] = message.useMessage();

  const registerMutation = useMutation({
    mutationFn: authApi.register,
    onSuccess: (data) => {
      login(data.token, { id: data.userId, name: data.name, email: data.email });
      messageApi.success("Conta criada com sucesso!");
    },
    onError: (error: any) => {
      messageApi.error(
        error.response?.data?.error || "Falha ao criar conta. Tente novamente."
      );
    },
  });

  const onFinish = (values: RegisterUserCommand) => {
    registerMutation.mutate(values);
  };

  return (
    <Card 
      bordered={false} 
      style={{ 
        boxShadow: "0 4px 6px -1px rgb(0 0 0 / 0.1), 0 2px 4px -2px rgb(0 0 0 / 0.1)" 
      }}
    >
      {contextHolder}
      <h2 style={{ fontSize: 24, fontWeight: 600, marginBottom: 24, textAlign: "center" }}>
        Criar Conta
      </h2>
      
      <Form
        form={form}
        layout="vertical"
        onFinish={onFinish}
        size="large"
      >
        <Form.Item
          name="name"
          rules={[{ required: true, message: "Por favor, insira seu nome!" }]}
        >
          <Input prefix={<UserOutlined />} placeholder="Nome completo" />
        </Form.Item>

        <Form.Item
          name="email"
          rules={[
            { required: true, message: "Por favor, insira seu email!" },
            { type: "email", message: "Email inválido!" }
          ]}
        >
          <Input prefix={<MailOutlined />} placeholder="Email" />
        </Form.Item>

        <Form.Item
          name="password"
          rules={[
            { required: true, message: "Por favor, insira sua senha!" },
            { min: 6, message: "A senha deve ter no mínimo 6 caracteres!" }
          ]}
        >
          <Input.Password prefix={<LockOutlined />} placeholder="Senha" />
        </Form.Item>

        <Form.Item>
          <Button 
            type="primary" 
            htmlType="submit" 
            block 
            loading={registerMutation.isPending}
          >
            Criar conta
          </Button>
        </Form.Item>
        
        <div style={{ textAlign: "center" }}>
          <span style={{ color: "var(--secondary-color)" }}>Já tem uma conta? </span>
          <Link href="/login" style={{ color: "var(--tertiary-color)", fontWeight: 500 }}>
            Entrar
          </Link>
        </div>
      </Form>
    </Card>
  );
}
