"use client";

import { useState } from "react";
import { Button, Typography, Space } from "antd";
import { PlusOutlined } from "@ant-design/icons";
import { TransactionTable } from "@/components/transactions/transaction-table";
import { TransactionForm } from "@/components/transactions/transaction-form";
import { TransactionResponse } from "@/lib/types";

const { Title } = Typography;

export default function TransactionsPage() {
  const [formOpen, setFormOpen] = useState(false);
  const [editingTransaction, setEditingTransaction] = useState<TransactionResponse | null>(null);

  const handleEdit = (transaction: TransactionResponse) => {
    setEditingTransaction(transaction);
    setFormOpen(true);
  };

  const handleCreate = () => {
    setEditingTransaction(null);
    setFormOpen(true);
  };

  const handleClose = () => {
    setFormOpen(false);
    setTimeout(() => setEditingTransaction(null), 300); // Wait for drawer animation
  };

  return (
    <Space direction="vertical" size="large" style={{ width: "100%" }}>
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
        <Title level={3} style={{ margin: 0 }}>Transações</Title>
        <Button 
          type="primary" 
          icon={<PlusOutlined />} 
          onClick={handleCreate}
          size="large"
          style={{ backgroundColor: "var(--tertiary-color)" }}
        >
          Nova
        </Button>
      </div>

      <TransactionTable onEdit={handleEdit} />

      <TransactionForm 
        open={formOpen} 
        onClose={handleClose} 
        transaction={editingTransaction} 
      />
    </Space>
  );
}
