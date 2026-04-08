"use client";

import { Card, Statistic, Row, Col } from "antd";
import { ArrowUpOutlined, ArrowDownOutlined } from "@ant-design/icons";
import { formatCurrency } from "@/lib/utils/format";
import { MonthlySummaryResponse } from "@/lib/types";

interface SummaryCardsProps {
  summary?: MonthlySummaryResponse | null;
  loading: boolean;
}

export function SummaryCards({ summary, loading }: SummaryCardsProps) {
  return (
    <Row gutter={[16, 16]}>
      <Col xs={24} md={8}>
        <Card 
          bordered={false} 
          loading={loading}
          style={{ borderTop: "4px solid var(--tertiary-color)", boxShadow: "0 2px 8px rgba(0,0,0,0.08)" }}
        >
          <Statistic
            title="Saldo Real (Conta)"
            value={summary?.closingBalance || 0}
            formatter={(val) => formatCurrency(Number(val))}
            valueStyle={{ color: "var(--primary-color)", fontWeight: "bold" }}
          />
        </Card>
      </Col>
      <Col xs={24} md={8}>
        <Card 
          bordered={false} 
          loading={loading}
          style={{ borderTop: "4px solid var(--warning-color)", boxShadow: "0 2px 8px rgba(0,0,0,0.08)" }}
        >
          <Statistic
            title="Saldo Previsto (Fim do Mês)"
            value={summary?.projectedBalance || 0}
            formatter={(val) => formatCurrency(Number(val))}
            valueStyle={{ color: "var(--primary-color)", fontWeight: "bold" }}
          />
        </Card>
      </Col>
      <Col xs={24} md={8}>
        <Card 
          bordered={false} 
          loading={loading}
          style={{ borderTop: "4px solid var(--secondary-color)", boxShadow: "0 2px 8px rgba(0,0,0,0.08)" }}
        >
          <Statistic
            title="Rollover (Mês Anterior)"
            value={summary?.openingBalance || 0}
            formatter={(val) => formatCurrency(Number(val))}
            valueStyle={{ color: "var(--secondary-color)" }}
          />
        </Card>
      </Col>
    </Row>
  );
}
