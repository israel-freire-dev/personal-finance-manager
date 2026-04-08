"use client";

import { useState } from "react";
import { Button, Typography, Space } from "antd";
import { PlusOutlined } from "@ant-design/icons";
import { RecurringTable } from "@/components/recurring/recurring-table";
import { RecurringForm } from "@/components/recurring/recurring-form";

const { Title } = Typography;

export default function RecurringPage() {
  const [formOpen, setFormOpen] = useState(false);

  return (
    <Space direction="vertical" size="large" style={{ width: "100%" }}>
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
        <Title level={3} style={{ margin: 0 }}>Despesas Fixas & Parcelamentos</Title>
        <Button 
          type="primary" 
          icon={<PlusOutlined />} 
          onClick={() => setFormOpen(true)}
          size="large"
          style={{ backgroundColor: "var(--tertiary-color)" }}
        >
          Novo
        </Button>
      </div>

      <RecurringTable />

      <RecurringForm 
        open={formOpen} 
        onClose={() => setFormOpen(false)} 
      />
    </Space>
  );
}
