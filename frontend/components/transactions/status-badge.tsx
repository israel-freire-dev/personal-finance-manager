import { Tag } from "antd";
import { TransactionStatus } from "@/lib/types";
import { STATUS_COLORS, STATUS_LABELS } from "@/lib/utils/constants";
import { 
  CheckCircleOutlined, 
  SyncOutlined, 
  CloseCircleOutlined, 
  RollbackOutlined, 
  StockOutlined 
} from "@ant-design/icons";

interface StatusBadgeProps {
  status: TransactionStatus;
}

export function StatusBadge({ status }: StatusBadgeProps) {
  const color = STATUS_COLORS[status];
  const label = STATUS_LABELS[status];

  let icon = null;
  switch (status) {
    case 'PAID':
      icon = <CheckCircleOutlined />;
      break;
    case 'PENDING':
      icon = <SyncOutlined spin />;
      break;
    case 'CANCELLED':
      icon = <CloseCircleOutlined />;
      break;
    case 'REFUNDED':
      icon = <RollbackOutlined />;
      break;
    case 'INVESTED':
      icon = <StockOutlined />;
      break;
  }

  return (
    <Tag 
      color={color} 
      icon={icon}
      style={{
        textDecoration: status === 'CANCELLED' ? 'line-through' : 'none',
        borderRadius: '16px',
        padding: '0 8px',
        fontWeight: 500,
      }}
    >
      {label}
    </Tag>
  );
}
