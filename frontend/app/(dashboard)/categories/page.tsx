"use client";

import { useState } from "react";
import { Button, Typography, Space, Row, Col, Card, Popconfirm, message } from "antd";
import { PlusOutlined, EditOutlined, DeleteOutlined, TagsOutlined } from "@ant-design/icons";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { categoryApi } from "@/lib/api/categories";
import { useAuth } from "@/components/providers/auth-provider";
import { CategoryResponse } from "@/lib/types";
import { CategoryForm } from "@/components/categories/category-form";

const { Title } = Typography;

export default function CategoriesPage() {
  const { user } = useAuth();
  const queryClient = useQueryClient();
  const [formOpen, setFormOpen] = useState(false);
  const [editingCategory, setEditingCategory] = useState<CategoryResponse | null>(null);

  const { data: categories, isLoading } = useQuery({
    queryKey: ["categories", user?.id],
    queryFn: () => categoryApi.findAllByUserId(user?.id as string),
    enabled: !!user?.id,
  });

  const deleteMutation = useMutation({
    mutationFn: categoryApi.delete,
    onSuccess: () => {
      message.success("Categoria excluída com sucesso");
      queryClient.invalidateQueries({ queryKey: ["categories"] });
    },
    onError: () => {
      message.error("Erro ao excluir. Verifique se existem transações vinculadas.");
    }
  });

  const handleEdit = (category: CategoryResponse) => {
    setEditingCategory(category);
    setFormOpen(true);
  };

  const incomes = categories?.filter(c => c.type === "INCOME") || [];
  const expenses = categories?.filter(c => c.type === "EXPENSE") || [];

  const renderCategoryCard = (category: CategoryResponse) => (
    <Col xs={24} sm={12} md={8} lg={6} key={category.id}>
      <Card 
        size="small" 
        style={{ 
          borderLeft: `4px solid ${category.type === 'INCOME' ? 'var(--success-color)' : 'var(--danger-color)'}`
        }}
        actions={[
          <EditOutlined key="edit" onClick={() => handleEdit(category)} />,
          <Popconfirm
            key="delete"
            title="Excluir categoria?"
            onConfirm={() => deleteMutation.mutate(category.id)}
            okText="Sim"
            cancelText="Não"
          >
            <DeleteOutlined style={{ color: "var(--danger-color)" }} />
          </Popconfirm>
        ]}
      >
        <div style={{ display: "flex", alignItems: "center", gap: 12 }}>
          <div style={{ 
            width: 32, 
            height: 32, 
            borderRadius: "50%", 
            background: "var(--neutral-color)", 
            display: "flex", 
            alignItems: "center", 
            justifyContent: "center",
            color: "var(--secondary-color)"
          }}>
            <TagsOutlined />
          </div>
          <span style={{ fontWeight: 500 }}>{category.name}</span>
        </div>
      </Card>
    </Col>
  );

  return (
    <Space direction="vertical" size="large" style={{ width: "100%" }}>
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
        <Title level={3} style={{ margin: 0 }}>Categorias</Title>
        <Button 
          type="primary" 
          icon={<PlusOutlined />} 
          onClick={() => { setEditingCategory(null); setFormOpen(true); }}
          size="large"
          style={{ backgroundColor: "var(--tertiary-color)" }}
        >
          Nova
        </Button>
      </div>

      <Card bordered={false} loading={isLoading} style={{ boxShadow: "0 2px 8px rgba(0,0,0,0.08)" }}>
        <Title level={5} style={{ color: "var(--danger-color)", marginTop: 0 }}>Despesas</Title>
        <Row gutter={[16, 16]}>
          {expenses.map(renderCategoryCard)}
          {expenses.length === 0 && <span style={{ color: "var(--secondary-color)", padding: 8 }}>Nenhuma despesa cadastrada.</span>}
        </Row>

        <Title level={5} style={{ color: "var(--success-color)", marginTop: 32 }}>Receitas</Title>
        <Row gutter={[16, 16]}>
          {incomes.map(renderCategoryCard)}
          {incomes.length === 0 && <span style={{ color: "var(--secondary-color)", padding: 8 }}>Nenhuma receita cadastrada.</span>}
        </Row>
      </Card>

      <CategoryForm 
        open={formOpen} 
        onClose={() => setFormOpen(false)} 
        category={editingCategory} 
      />
    </Space>
  );
}
