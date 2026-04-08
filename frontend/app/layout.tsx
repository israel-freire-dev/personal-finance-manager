import type { Metadata } from "next";
import { ThemeProvider } from "@/components/providers/theme-provider";
import QueryProvider from "@/components/providers/query-provider";
import { AuthProvider } from "@/components/providers/auth-provider";
import "./globals.css";

import { NuqsAdapter } from 'nuqs/adapters/next/app';

export const metadata: Metadata = {
  title: "Slate Capital",
  description: "Personal Finance Manager",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="pt-BR">
      <body>
        <ThemeProvider>
          <QueryProvider>
            <AuthProvider>
              <NuqsAdapter>
                {children}
              </NuqsAdapter>
            </AuthProvider>
          </QueryProvider>
        </ThemeProvider>
      </body>
    </html>
  );
}
