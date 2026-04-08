import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  transpilePackages: ['antd', '@ant-design/icons', '@ant-design/charts', 'rc-util', '@ant-design/cssinjs'],
};

export default nextConfig;
