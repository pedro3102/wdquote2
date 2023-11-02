import axios, { AxiosError, AxiosInstance, AxiosResponse } from 'axios'
import { localStorageInstance } from '@/shared/services/storage/local-storage-service'
import { sessionStorageInstance } from '@/shared/services/storage/session-storage-service'

interface IConfig {
  withCredentials: boolean
}

const config: IConfig = {
  withCredentials: true,
}

const TIMEOUT = 15000
const onRequestSuccess = config => {
  const token = localStorageInstance.getItem('authenticationToken') || sessionStorageInstance.getItem('authenticationToken')
  if (token) {
    if (!config.headers) {
      config.headers = {}
    }
    config.headers.Authorization = `Bearer ${token}`
  }
  config.timeout = TIMEOUT
  config.url = `${SERVER_API_URL}${config.url}`
  return config
}

export const HttpClient: AxiosInstance = axios.create(config)

export const configInterceptors = (onUnauthenticated, onServerError) => {
  const onResponseError = (err: AxiosError): Promise<number> => {
    const { response } = err
    if (response?.status === 403 || response?.status === 401) {
      return onUnauthenticated(err)
    }
    if (response?.status && response.status >= 500) {
      return onServerError(err)
    }
    return Promise.reject(err)
  }
  if (HttpClient.interceptors) {
    HttpClient.interceptors.request.use(onRequestSuccess)
    HttpClient.interceptors.response.use((res: AxiosResponse) => res, onResponseError)
  }
}

// We set content-type to json by default
// HttpClient.defaults.headers['Content-Type'] = 'application/json'

/** Auth token interceptors */
/** TODO: Add auth token */
// const authInterceptor = () => config;

/** logger interceptors */
/** TODO: Add logger config */
// const loggerInterceptor = () => config;

/** Adding the request interceptors */
// HttpClient.interceptors.request.use(authInterceptor);
// HttpClient.interceptors.request.use(loggerInterceptor);
// HttpClient.interceptors.response.use(responseInterceptor, errorInterceptor)

export default HttpClient
