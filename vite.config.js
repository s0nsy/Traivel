import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/api': {
        target: 'https://api.routeport.co.kr',
        changeOrigin: true,
        secure: true, // HTTPS 요청인 경우 true
        rewrite: (path) => path.replace(/^\/api/, ''), // '/api' 제거하고 전달
      },
    },
  },
});