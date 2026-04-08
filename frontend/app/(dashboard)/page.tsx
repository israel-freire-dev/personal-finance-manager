"use client";

import { useState } from "react";
import { DatePicker, Card, Space, Typography, Row, Col, Alert } from "antd";
import { useQuery } from "@tanstack/react-query";
import dayjs, { Dayjs } from "dayjs";
import { summaryApi } from "@/lib/api/summaries";
import { useAuth } from "@/components/providers/auth-provider";
import { SummaryCards } from "@/components/dashboard/summary-cards";
import { ExpenseChart } from "@/components/dashboard/expense-chart";

const { Title } = Typography;

export default function DashboardPage() {
  const { user } = useAuth();
  const [selectedDate, setSelectedDate] = useState<Dayjs>(dayjs());

  const month = selectedDate.month() + 1; // 1-12
  const year = selectedDate.year();

  const { data: summary, isLoading, isError, error } = useQuery({
    queryKey: ["summary", user?.id, month, year],
    queryFn: () => summaryApi.getSummary(user?.id as string, month, year),
    enabled: !!user?.id,
  });

  return (
    <Space direction="vertical" size="large" style={{ width: "100%" }}>
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", flexWrap: "wrap", gap: "16px" }}>
        <Title level={3} style={{ margin: 0 }}>Dashboard</Title>
        <DatePicker 
          picker="month" 
          value={selectedDate} 
          onChange={(date) => date && setSelectedDate(date)} 
          format="MMMM [de] YYYY"
          allowClear={false}
          size="large"
        />
      </div>

      {isError && (
        <Alert 
          message="Erro ao carregar o resumo mensal" 
          description={(error as any)?.message || "Tente novamente mais tarde."}
          type="error" 
          showIcon 
        />
      )}

      <SummaryCards summary={summary} loading={isLoading} />

      <Row gutter={[16, 16]}>
        <Col xs={24} lg={16}>
          <Card 
            title="Receitas x Despesas" 
            bordered={false} 
            loading={isLoading}
            style={{ boxShadow: "0 2px 8px rgba(0,0,0,0.08)", minHeight: 400 }}
          >
            <ExpenseChart summary={summary} />
          </Card>
        </Col>
        <Col xs={24} lg={8}>
          <Card 
            title="Dicas Financeiras" 
            bordered={false} 
            loading={isLoading}
            style={{ boxShadow: "0 2px 8px rgba(0,0,0,0.08)", minHeight: 400 }}
          >
            <p><strong>Acompanhe seu saldo:</strong> Fique de olho no seu saldo previsto para não ter surpresas no fim do mês.</p>
            <p><strong>Categorize tudo:</strong> Manter suas transações com a categoria certa ajuda a entender onde você pode economizar.</p>
          </Card>
        </Col>
      </Row>
    </Space>
  );
}
