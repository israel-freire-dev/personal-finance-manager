"use client";

import { Layout, Button, Avatar, Dropdown } from "antd";
import { 
  MenuFoldOutlined, 
  MenuUnfoldOutlined, 
  UserOutlined 
} from "@ant-design/icons";
import { useAuth } from "../providers/auth-provider";

const { Header } = Layout;

interface AppHeaderProps {
  collapsed: boolean;
  setCollapsed: (val: boolean) => void;
}

export function AppHeader({ collapsed, setCollapsed }: AppHeaderProps) {
  const { user, logout } = useAuth();

  const userMenu = [
    {
      key: "name",
      label: <div style={{ fontWeight: 600 }}>{user?.name}</div>,
      disabled: true,
    },
    {
      type: "divider" as const,
    },
    {
      key: "logout",
      label: "Sair",
      danger: true,
      onClick: logout,
    },
  ];

  return (
    <Header 
      style={{ 
        padding: "0 16px", 
        background: "var(--colorBgContainer)",
        display: "flex",
        alignItems: "center",
        justifyContent: "space-between",
        boxShadow: "0 1px 4px 0 rgba(0,21,41,.08)",
        zIndex: 10
      }}
    >
      <div style={{ display: "flex", alignItems: "center" }}>
        <Button
          type="text"
          icon={collapsed ? <MenuUnfoldOutlined /> : <MenuFoldOutlined />}
          onClick={() => setCollapsed(!collapsed)}
          style={{
            fontSize: '16px',
            width: 64,
            height: 64,
          }}
        />
      </div>

      <div>
        <Dropdown menu={{ items: userMenu }} placement="bottomRight" arrow>
          <div style={{ cursor: "pointer", display: "flex", alignItems: "center", gap: 8 }}>
            <Avatar style={{ backgroundColor: "var(--tertiary-color)" }} icon={<UserOutlined />} />
            <span style={{ display: "none" }} className="md:inline-block">
              {user?.name?.split(" ")[0]}
            </span>
          </div>
        </Dropdown>
      </div>
    </Header>
  );
}
