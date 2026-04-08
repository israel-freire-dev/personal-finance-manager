"use client";

import { Layout, Menu } from "antd";
import { 
  DashboardOutlined, 
  SwapOutlined, 
  CalendarOutlined, 
  TagsOutlined,
  LogoutOutlined
} from "@ant-design/icons";
import { useRouter, usePathname } from "next/navigation";
import { useAuth } from "../providers/auth-provider";

const { Sider } = Layout;

interface AppSidebarProps {
  collapsed: boolean;
  setCollapsed: (val: boolean) => void;
  isMobile: boolean;
}

export function AppSidebar({ collapsed, setCollapsed, isMobile }: AppSidebarProps) {
  const router = useRouter();
  const pathname = usePathname();
  const { logout } = useAuth();

  const menuItems = [
    {
      key: "/",
      icon: <DashboardOutlined />,
      label: "Dashboard",
    },
    {
      key: "/transactions",
      icon: <SwapOutlined />,
      label: "Transações",
    },
    {
      key: "/recurring",
      icon: <CalendarOutlined />,
      label: "Recorrentes",
    },
    {
      key: "/categories",
      icon: <TagsOutlined />,
      label: "Categorias",
    },
    {
      type: "divider" as const,
    },
    {
      key: "logout",
      icon: <LogoutOutlined />,
      label: "Sair",
      danger: true,
    },
  ];

  const handleMenuClick = ({ key }: { key: string }) => {
    if (key === "logout") {
      logout();
    } else {
      router.push(key);
      if (isMobile) {
        setCollapsed(true);
      }
    }
  };

  return (
    <Sider 
      trigger={null} 
      collapsible 
      collapsed={collapsed}
      breakpoint="md"
      collapsedWidth={isMobile ? 0 : 80}
      onBreakpoint={(broken) => {
        if (broken) {
          setCollapsed(true);
        }
      }}
      style={{
        overflow: 'auto',
        height: '100vh',
        position: isMobile ? 'absolute' : 'relative',
        zIndex: 1000,
        boxShadow: "2px 0 8px 0 rgba(29,35,41,.05)"
      }}
    >
      <div 
        style={{ 
          height: 64, 
          display: "flex", 
          alignItems: "center", 
          justifyContent: "center",
          color: "white",
          fontWeight: "bold",
          fontSize: collapsed ? 14 : 20,
          whiteSpace: "nowrap",
          overflow: "hidden",
          transition: "all 0.2s"
        }}
      >
        {collapsed ? "SC" : "Slate Capital"}
      </div>
      <Menu
        theme="dark"
        mode="inline"
        selectedKeys={[pathname]}
        items={menuItems}
        onClick={handleMenuClick}
      />
    </Sider>
  );
}
