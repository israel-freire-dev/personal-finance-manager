"use client";

import { MonthlySummaryResponse } from "@/lib/types";
import { formatCurrency } from "@/lib/utils/format";
import dynamic from 'next/dynamic';

const Column = dynamic(() => import('@ant-design/plots').then(mod => mod.Column), { ssr: false });

interface ExpenseChartProps {
  summary?: MonthlySummaryResponse | null;
}

export function ExpenseChart({ summary }: ExpenseChartProps) {
  if (!summary) return null;

  const data = [
    { type: 'Receitas', value: summary.totalIncome || 0, category: 'Real' },
    { type: 'Despesas', value: summary.totalExpenses || 0, category: 'Real' },
    { type: 'Receitas', value: summary.totalIncomePending || 0, category: 'Pendente' },
    { type: 'Despesas', value: summary.totalExpensesPending || 0, category: 'Pendente' },
  ].filter(d => d.value > 0);

  if (data.length === 0) {
    return <div style={{ padding: "40px 0", textAlign: "center", color: "var(--secondary-color)" }}>Sem dados para exibir</div>;
  }

  const config = {
    data,
    xField: 'type',
    yField: 'value',
    colorField: 'type',
    seriesField: 'category',
    isGroup: true,
    columnStyle: {
      radius: [4, 4, 0, 0],
    },
    color: ({ type }: { type: string }) => {
      if (type === 'Receitas') return '#10B981'; // Success
      return '#EF4444'; // Danger
    },
    label: {
      position: 'top',
      formatter: (datum: any) => formatCurrency(datum.value),
    },
    legend: {
      position: 'top-left',
    },
    tooltip: {
      formatter: (datum: any) => {
        return { name: `${datum.category}`, value: formatCurrency(datum.value) };
      },
    },
  };

  return <Column {...(config as any)} />;
}
