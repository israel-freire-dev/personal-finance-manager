import { ReactNode } from "react";
import { Layout } from "antd";

const { Content } = Layout;

export default function AuthLayout({ children }: { children: ReactNode }) {
  return (
    <Layout style={{ minHeight: "100vh", backgroundColor: "var(--neutral-color)" }}>
      <Content style={{ display: "flex", justifyContent: "center", alignItems: "center", padding: "20px" }}>
        <div style={{ width: "100%", maxWidth: 400 }}>
          <div style={{ textAlign: "center", marginBottom: 32 }}>
            <h1 style={{ fontSize: 32, fontWeight: 700, color: "var(--primary-color)", margin: 0 }}>
              Slate Capital
            </h1>
            <p style={{ color: "var(--secondary-color)", marginTop: 8 }}>
              Personal Finance Manager
            </p>
          </div>
          {children}
        </div>
      </Content>
    </Layout>
  );
}
