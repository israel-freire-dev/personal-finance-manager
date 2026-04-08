"use client";

import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { recurringApi } from "@/lib/api/recurring";
import { useAuth } from "@/components/providers/auth-provider";
import { Table, Button, Card, Space, Popconfirm, message, Tag } from "antd";
import { DeleteOutlined } from "@ant-design/icons";
import { formatDate, formatCurrency } from "@/lib/utils/format";
import { RecurringTemplateResponse } from "@/lib/types";
import { FREQUENCY_LABELS } from "@/lib/utils/constants";

export function RecurringTable() {
  const { user } = useAuth();
  const queryClient = useQueryClient();

  const { data: templates, isLoading } = useQuery({
    queryKey: ["recurring", user?.id],
    queryFn: () => recurringApi.findAllByUserId(user?.id as string),
    enabled: !!user?.id,
  });

  const deleteMutation = useMutation({
    mutationFn: recurringApi.delete,
    onSuccess: () => {
      message.success("Cancelado com sucesso. Transações futuras apagadas.");
      queryClient.invalidateQueries({ queryKey: ["recurring"] });
      queryClient.invalidateQueries({ queryKey: ["transactions"] });
      queryClient.invalidateQueries({ queryKey: ["summary"] });
    },
    onError: () => {
      message.error("Erro ao cancelar.");
    }
  });

  const columns = [
    {
      title: 'Descrição',
      dataIndex: 'description',
      key: 'description',
    },
    {
      title: 'Valor Base',
      dataIndex: 'amount',
      key: 'amount',
      render: (amount: number) => <strong>{formatCurrency(amount)}</strong>,
    },
    {
      title: 'Frequência',
      dataIndex: 'frequency',
      key: 'frequency',
      render: (freq: string) => <Tag color="blue">{FREQUENCY_LABELS[freq as keyof typeof FREQUENCY_LABELS]}</Tag>,
    },
    {
      title: 'Início',
      dataIndex: 'startDate',
      key: 'startDate',
      render: (date: string) => formatDate(date),
    },
    {
      title: 'Parcelas',
      dataIndex: 'totalInstallments',
      key: 'totalInstallments',
      render: (total: number | null) => total ? `${total}x` : <span style={{ color: 'var(--secondary-color)' }}>Perene</span>,
    },
    {
      title: 'Ações',
      key: 'actions',
      render: (_: any, record: RecurringTemplateResponse) => (
        <Space>
          <Popconfirm
            title="Tem certeza que deseja cancelar essa recorrência?"
            description="Transações futuras pendentes serão excluídas."
            onConfirm={() => deleteMutation.mutate(record.id)}
            okText="Sim, Cancelar"
            cancelText="Não"
            okButtonProps={{ danger: true }}
          >
            <Button 
              type="text" 
              danger 
              icon={<DeleteOutlined />} 
            >
              Cancelar Contrato
            </Button>
          </Popconfirm>
        </Space>
      ),
    },
  ];

  return (
    <Card bordered={false} style={{ boxShadow: "0 2px 8px rgba(0,0,0,0.08)" }}>
      <Table 
        dataSource={templates} 
        columns={columns} 
        rowKey="id"
        loading={isLoading}
        pagination={{ pageSize: 10 }}
        scroll={{ x: true }}
      />
    </Card>
  );
}
