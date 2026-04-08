"use client";

import { useState } from "react";
import { Card, Form, Input, Button, message } from "antd";
import { UserOutlined, LockOutlined } from "@ant-design/icons";
import Link from "next/link";
import { useMutation } from "@tanstack/react-query";
import { authApi } from "@/lib/api/auth";
import { LoginCommand } from "@/lib/types";
import { useAuth } from "@/components/providers/auth-provider";

export default function LoginPage() {
  const [form] = Form.useForm<LoginCommand>();
  const { login } = useAuth();
  const [messageApi, contextHolder] = message.useMessage();

  const loginMutation = useMutation({
    mutationFn: authApi.login,
    onSuccess: (data) => {
      login(data.token, { id: data.userId, name: data.name, email: data.email });
    },
    onError: (error: any) => {
      messageApi.error(
        error.response?.data?.error || "Falha ao realizar login. Verifique suas credenciais."
      );
    },
  });

  const onFinish = (values: LoginCommand) => {
    loginMutation.mutate(values);
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
        Entrar
      </h2>
      
      <Form
        form={form}
        layout="vertical"
        onFinish={onFinish}
        size="large"
      >
        <Form.Item
          name="email"
          rules={[
            { required: true, message: "Por favor, insira seu email!" },
            { type: "email", message: "Email inválido!" }
          ]}
        >
          <Input prefix={<UserOutlined />} placeholder="Email" />
        </Form.Item>

        <Form.Item
          name="password"
          rules={[{ required: true, message: "Por favor, insira sua senha!" }]}
        >
          <Input.Password prefix={<LockOutlined />} placeholder="Senha" />
        </Form.Item>

        <Form.Item>
          <Button 
            type="primary" 
            htmlType="submit" 
            block 
            loading={loginMutation.isPending}
          >
            Entrar
          </Button>
        </Form.Item>
        
        <div style={{ textAlign: "center" }}>
          <span style={{ color: "var(--secondary-color)" }}>Não tem uma conta? </span>
          <Link href="/register" style={{ color: "var(--tertiary-color)", fontWeight: 500 }}>
            Registre-se
          </Link>
        </div>
      </Form>
    </Card>
  );
}
