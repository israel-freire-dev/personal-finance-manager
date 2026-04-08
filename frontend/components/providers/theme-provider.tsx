"use client";

import { ConfigProvider } from "antd";
import { ReactNode } from "react";
import ptBR from "antd/locale/pt_BR";

const slateCapitalTheme = {
  token: {
    colorPrimary: "#0D9488",
    colorBgBase: "#F8FAFC",
    colorTextBase: "#1E293B",
    colorBgContainer: "#FFFFFF",
    colorBorder: "#E2E8F0",
    colorBorderSecondary: "#CBD5E1",
    fontFamily: "'Inter', -apple-system, sans-serif",
    borderRadius: 8,
    colorSuccess: "#10B981",
    colorWarning: "#F59E0B",
    colorError: "#EF4444",
    colorInfo: "#0D9488",
  },
  components: {
    Layout: {
      siderBg: "#1E293B",
      headerBg: "#FFFFFF",
    },
    Menu: {
      darkItemBg: "#1E293B",
      darkItemSelectedBg: "#0D9488",
    },
  },
};

export function ThemeProvider({ children }: { children: ReactNode }) {
  return (
    <ConfigProvider theme={slateCapitalTheme} locale={ptBR}>
      {children}
    </ConfigProvider>
  );
}
