import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/api': {

        target: 'https://api.routeport.co.kr', // 수정된 API 경로
        changeOrigin: true,
        secure: true, // HTTPS 서버로 프록시하는 경우 필요
        rewrite: (path) => path.replace(/^\/api/, ''), // /api를 제거하여 요청을 /routes로 보냄
      },
    },
  },
});