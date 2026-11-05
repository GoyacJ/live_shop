const configuredBaseUrl = import.meta.env.VITE_API_BASE_URL

export const API_BASE_URL = configuredBaseUrl || '/backend-api'

export function apiUrl(path) {
  const baseUrl = API_BASE_URL.replace(/\/$/, '')
  const requestPath = path.replace(/^\//, '')
  return `${baseUrl}/${requestPath}`
}
