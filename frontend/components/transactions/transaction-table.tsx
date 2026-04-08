"use client";

import { useQuery } from "@tanstack/react-query";
import { transactionApi } from "@/lib/api/transactions";
import { useAuth } from "@/components/providers/auth-provider";
import { Table, Button, Card, Space, Popconfirm, message } from "antd";
import { EditOutlined, DeleteOutlined } from "@ant-design/icons";
import { StatusBadge } from "./status-badge";
import { formatDate, formatCurrency } from "@/lib/utils/format";
import { TransactionResponse } from "@/lib/types";
import { parseAsInteger, parseAsString, useQueryState, parseAsStringEnum } from 'nuqs';
import { TYPE_LABELS } from "@/lib/utils/constants";
import { useMutation, useQueryClient } from "@tanstack/react-query";

interface TransactionTableProps {
  onEdit: (transaction: TransactionResponse) => void;
}

export function TransactionTable({ onEdit }: TransactionTableProps) {
  const { user } = useAuth();
  const queryClient = useQueryClient();
  const [statusFilter, setStatusFilter] = useQueryState('status', parseAsString);
  const [typeFilter, setTypeFilter] = useQueryState('type', parseAsString);

  const { data: transactions, isLoading } = useQuery({
    queryKey: ["transactions", user?.id],
    queryFn: () => transactionApi.findAllByUserId(user?.id as string),
    enabled: !!user?.id,
  });

  const deleteMutation = useMutation({
    mutationFn: transactionApi.delete,
    onSuccess: () => {
      message.success("Transação excluída com sucesso");
      queryClient.invalidateQueries({ queryKey: ["transactions"] });
      queryClient.invalidateQueries({ queryKey: ["summary"] });
    },
    onError: () => {
      message.error("Erro ao excluir transação");
    }
  });

  const filteredData = transactions?.filter(t => {
    if (statusFilter && t.status !== statusFilter) return false;
    if (typeFilter && t.type !== typeFilter) return false;
    return true;
  });

  const columns = [
    {
      title: 'Data',
      dataIndex: 'date',
      key: 'date',
      render: (date: string) => formatDate(date),
      sorter: (a: TransactionResponse, b: TransactionResponse) => new Date(a.date).getTime() - new Date(b.date).getTime(),
    },
    {
      title: 'Descrição',
      dataIndex: 'description',
      key: 'description',
    },
    {
      title: 'Tipo',
      dataIndex: 'type',
      key: 'type',
      render: (type: string) => (
        <span style={{ color: type === 'INCOME' ? 'var(--success-color)' : 'var(--danger-color)', fontWeight: 500 }}>
          {TYPE_LABELS[type as keyof typeof TYPE_LABELS]}
        </span>
      ),
      filters: [
        { text: 'Receita', value: 'INCOME' },
        { text: 'Despesa', value: 'EXPENSE' },
      ],
      onFilter: (value: boolean | React.Key, record: TransactionResponse) => record.type === value,
    },
    {
      title: 'Valor',
      dataIndex: 'amount',
      key: 'amount',
      render: (amount: number, record: TransactionResponse) => (
        <span style={{ color: record.type === 'INCOME' ? 'var(--success-color)' : 'inherit', fontWeight: 'bold' }}>
          {record.type === 'EXPENSE' ? '-' : '+'} {formatCurrency(amount)}
        </span>
      ),
      sorter: (a: TransactionResponse, b: TransactionResponse) => a.amount - b.amount,
    },
    {
      title: 'Status',
      dataIndex: 'status',
      key: 'status',
      render: (status: any) => <StatusBadge status={status} />,
      filters: [
        { text: 'Pago', value: 'PAID' },
        { text: 'Pendente', value: 'PENDING' },
        { text: 'Cancelado', value: 'CANCELLED' },
        { text: 'Investido', value: 'INVESTED' },
        { text: 'Reembolsado', value: 'REFUNDED' },
      ],
      onFilter: (value: boolean | React.Key, record: TransactionResponse) => record.status === value,
    },
    {
      title: 'Ações',
      key: 'actions',
      render: (_: any, record: TransactionResponse) => (
        <Space>
          <Button 
            type="text" 
            icon={<EditOutlined style={{ color: "var(--tertiary-color)" }} />} 
            onClick={() => onEdit(record)}
          />
          <Popconfirm
            title="Tem certeza que deseja excluir?"
            onConfirm={() => deleteMutation.mutate(record.id)}
            okText="Sim"
            cancelText="Não"
          >
            <Button 
              type="text" 
              danger 
              icon={<DeleteOutlined />} 
            />
          </Popconfirm>
        </Space>
      ),
    },
  ];

  return (
    <Card bordered={false} style={{ boxShadow: "0 2px 8px rgba(0,0,0,0.08)" }}>
      <Table 
        dataSource={filteredData} 
        columns={columns} 
        rowKey="id"
        loading={isLoading}
        pagination={{ pageSize: 10 }}
        scroll={{ x: true }}
      />
    </Card>
  );
}
