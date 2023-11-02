import useRequest, { IResponse } from '@/shared/api/http/request'
import ApiConstants from '@/shared/const/api-constants'

const request = useRequest()

class EntityService<T> {
  public apiPath = `${ApiConstants.API_VERSION_1}`

  public addQueryParams = (url: string, params?: any): string => request.addQueryParams(url, params)

  public list = async (url: string): Promise<IResponse<T[]>> => request.doGet(url)

  public save = async (baseUrl: string, postData: any): Promise<IResponse<T>> => request.doPost(baseUrl, postData)

  public update = async (baseUrl: string, putData: any): Promise<IResponse<T>> => request.doPut(baseUrl, putData)

  public patch = async (baseUrl: string, patchData: any, config?: any): Promise<IResponse<T>> => {
    const patchConfig = {
      ...config,
      headers: { 'Content-Type': 'application/merge-patch+json' },
    }
    return request.doPatch(baseUrl, patchData, patchConfig)
  }

  public find = async (baseUrl: string, id: number | string, config?: any): Promise<IResponse<T>> =>
    request.doGet(`${baseUrl}/${id}`, config)

  public get = async (baseUrl: string, config?: any): Promise<IResponse<T>> => request.doGet(baseUrl, null, config)

  public post = async (baseUrl: string, postData?: any, config?: any): Promise<IResponse<T>> => request.doPost(baseUrl, postData, config)

  public put = async (baseUrl: string, postData?: any, config?: any): Promise<IResponse<T>> => request.doPut(baseUrl, postData, config)

  public remove = async (baseUrl: string, id: number): Promise<IResponse<T>> => request.doDelete(`${baseUrl}/${id}`)

  public download = async (baseUrl: string, config?: any): Promise<IResponse<T>> => request.doDownload(baseUrl, config)
}

export default new EntityService<any>()
