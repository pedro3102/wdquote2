// import { alertService } from '@/shared/services/alert-service'
import appConstants from '@/shared/const/app-constants';
import { HttpClient } from './http-client';

/**
 * Request methods
 * @type {{DELETE: string, POST: string, GET: string, PUT: string, PATCH: string}}
 */
export enum Methods {
  GET = 'GET',
  PUT = 'PUT',
  POST = 'POST',
  PATCH = 'PATCH',
  DELETE = 'DELETE',
}

/**
 * Return the page size from the entity, return default global page size: 15
 * @param pageSize
 * @returns {number}
 */
export const getPageSize = (pageSize: number): number => pageSize || appConstants.APP.PAGE_SIZE;

/**
 * Add query params to the passed url.
 * @param url
 * @param params
 * @returns {*}
 */
const addQueryParams = (url: string, params?: object): string => {
  if (params) {
    let copyUrl = `${url}?`;
    Object.entries(params).forEach(([key, param], index, arr) => {
      copyUrl = `${copyUrl}${key}=${param}${arr[index + 1] ? '&' : ''}`;
    });
    return copyUrl;
  }
  return url;
};

/**
 * Extract total count response header, return zero if not found.
 * @param response
 * @returns {number}
 */
const getTotalCount = (response): number => {
  if (typeof response.headers['x-total-count'] !== 'undefined') {
    return parseInt(response.headers['x-total-count'], 10);
  }
  return 0;
};

interface IBaseResponse<T = any> {
  data: T;
  status?: number;
  statusText?: string;
  headers: any;
  request?: any;
}

export interface IResponse<T> extends IBaseResponse {
  xTotalCount?: number;
}

const useRequest = () => {
  const doRequest = async (method: Methods = Methods.GET, url: string, requestData: any, config): Promise<IResponse<any>> => {
    let response: IResponse<any>;
    let xTotalCount = 0;
    try {
      switch (method) {
        case Methods.POST:
          response = await HttpClient.post(url, requestData, config);
          break;
        case Methods.PUT:
          response = await HttpClient.put(url, requestData, config);
          break;
        case Methods.PATCH:
          response = await HttpClient.patch(url, requestData, config);
          break;
        case Methods.DELETE:
          response = await HttpClient.delete(url, config);
          break;
        default:
          response = await HttpClient.get(url, config);
      }
      if (response?.data) {
        xTotalCount = getTotalCount(response);
        return {
          ...response,
          xTotalCount,
        };
      }
      return response;
    } catch (exception) {
      //TODO agregar el servicio de alerta
      console.log(exception);
      // await alertService.showHttpError(exception)
      throw exception;
    }
  };

  const doGet = async (url: string, requestData?: any, config?: any): Promise<IResponse<any>> => doRequest(Methods.GET, url, null, config);

  const doPost = async (url: string, requestData: any, config?: any): Promise<IResponse<any>> =>
    doRequest(Methods.POST, url, requestData, config);

  const doPut = async (url: string, requestData: any, config?: any): Promise<IResponse<any>> =>
    doRequest(Methods.PUT, url, requestData, config);

  const doPatch = async (url: string, requestData: any, config?: any): Promise<IResponse<any>> =>
    doRequest(Methods.PATCH, url, requestData, config);

  const doDelete = async (url: string, config?: any): Promise<IResponse<any>> => doRequest(Methods.DELETE, url, null, config);

  const doDownload = async (url: string, config: any): Promise<IResponse<any>> => HttpClient.get(url, { responseType: 'blob', ...config });

  return {
    doGet,
    doPut,
    doPost,
    doPatch,
    doDelete,
    doDownload,
    addQueryParams,
  };
};

export default useRequest;
