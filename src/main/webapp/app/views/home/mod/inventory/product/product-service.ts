import { IResponse } from '@/shared/api/http/request'
import EntityService from '@/views/entities/common/entity-service'
import { productRouteName } from '@/views/home/mod/inventory/product/product-entity'
import { IProduct } from '@/views/home/mod/inventory/product/product-model'

const baseUrl = `${EntityService.apiPath}/${productRouteName}`

type EntityResponseType = IResponse<IProduct>
type EntityArrayResponseType = IResponse<IProduct[]>

class ProductService {
  public baseUrl: string = baseUrl

  public buildQuery = (params: object): string => EntityService.addQueryParams(baseUrl, params)
  public list = async (url: string): Promise<EntityArrayResponseType> => EntityService.list(url)
  public listBasic = async (url: string): Promise<EntityArrayResponseType> => EntityService.list(url)
  //public listBasic = async () => EntityService.get(baseUrl + '/basic')
  public find = (login: string): Promise<EntityResponseType> => EntityService.find(baseUrl, login)
  public save = (data: object): Promise<EntityResponseType> => EntityService.save(baseUrl, data)
  public update = (data: object, id: number): Promise<EntityResponseType> => EntityService.update(`${baseUrl}/${id}`, data)
  public patch = (data: IProduct): Promise<EntityResponseType> => EntityService.patch(`${baseUrl}/${data.id}`, data)
  public remove = async (id: number): Promise<IResponse<void>> => EntityService.remove(baseUrl, id)
}

export default new ProductService()
