import { defineConfig, loadEnv } from 'vite'
import uniModule from '@dcloudio/vite-plugin-uni'

const uni = typeof uniModule === 'function'
  ? uniModule
  : (uniModule.default || uniModule)

process.env.UNI_INPUT_DIR ||= process.cwd()

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '')
  const port = Number(env.VITE_PORT || 5173)
  const apiTarget = env.VITE_API_PROXY_TARGET || 'http://127.0.0.1:8081'

  return {
    plugins: [uni()],
    server: {
      host: '127.0.0.1',
      port,
      strictPort: true,
      proxy: {
        '/backend-api': {
          target: apiTarget,
          changeOrigin: true,
          rewrite: (path) => path.replace(/^\/backend-api/, '')
        }
      }
    },
    preview: {
      host: '127.0.0.1',
      port
    }
  }
})
