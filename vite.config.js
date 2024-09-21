import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      
      '/api': {
        target: 'https://routeport.co.kr',
        changeOrigin: true,
        secure: true, // HTTPS 서버로 프록시하는 경우 필요
        
      },
    },
  },
})