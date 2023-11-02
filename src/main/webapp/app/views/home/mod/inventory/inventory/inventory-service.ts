import { IResponse } from '@/shared/api/http/request'
import EntityService from '@/views/entities/common/entity-service'
import { inventoryRouteName } from '@/views/home/mod/inventory/inventory/inventory-entity'
import { IInventory } from '@/views/home/mod/inventory/inventory/inventory-model'

const baseUrl = `${EntityService.apiPath}/${inventoryRouteName}`

type EntityResponseType = IResponse<IInventory>
type EntityArrayResponseType = IResponse<IInventory[]>

class InventoryService {
  public baseUrl: string = baseUrl

  public buildQuery = (params: object): string => EntityService.addQueryParams(baseUrl, params)
  public list = async (url: string): Promise<EntityArrayResponseType> => EntityService.list(url)
  public find = (login: string): Promise<EntityResponseType> => EntityService.find(baseUrl, login)
  public save = (data: object): Promise<EntityResponseType> => EntityService.save(baseUrl, data)
  public update = (data: object, id: number): Promise<EntityResponseType> => EntityService.update(`${baseUrl}/${id}`, data)
  public patch = (data: IInventory): Promise<EntityResponseType> => EntityService.patch(`${baseUrl}/${data.id}`, data)
  public remove = async (id: number): Promise<IResponse<void>> => EntityService.remove(baseUrl, id)
}

export default new InventoryService()
