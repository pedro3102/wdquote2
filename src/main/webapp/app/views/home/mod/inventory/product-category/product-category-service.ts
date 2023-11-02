import { IResponse } from '@/shared/api/http/request'
import EntityService from '@/views/entities/common/entity-service'
import { productCategoryRouteName } from '@/views/home/mod/inventory/product-category/product-category-entity'
import { IProductCategory } from '@/views/home/mod/inventory/product-category/product-category-model'

const baseUrl = `${EntityService.apiPath}/${productCategoryRouteName}`

type EntityResponseType = IResponse<IProductCategory>
type EntityArrayResponseType = IResponse<IProductCategory[]>

class ProductCategoryService {
  public baseUrl: string = baseUrl

  public buildQuery = (params: object): string => EntityService.addQueryParams(baseUrl, params)
  public list = async (url: string): Promise<EntityArrayResponseType> => EntityService.list(url)
  public listBasic = async () => EntityService.get(baseUrl + '/basic')
  public find = (id: number): Promise<EntityResponseType> => EntityService.find(baseUrl, id)
  public save = (data: object): Promise<EntityResponseType> => EntityService.save(baseUrl, data)
  public update = (data: object, id: number): Promise<EntityResponseType> => EntityService.update(`${baseUrl}/${id}`, data)
  public patch = (data: IProductCategory): Promise<EntityResponseType> => EntityService.patch(`${baseUrl}/${data.id}`, data)
  public remove = async (id: number): Promise<IResponse<void>> => EntityService.remove(baseUrl, id)
}

export default new ProductCategoryService()
