import { IResponse } from '@/shared/api/http/request'
import EntityService from '@/views/entities/common/entity-service'
import { deliveryZoneRouteName } from '@/views/home/mod/inventory/delivery-zone/delivery-zone-entity'
import { IDeliveryZone } from '@/views/home/mod/inventory/delivery-zone/delivery-zone-model'

const baseUrl = `${EntityService.apiPath}/${deliveryZoneRouteName}`

type EntityResponseType = IResponse<IDeliveryZone>
type EntityArrayResponseType = IResponse<IDeliveryZone[]>

class DeliveryZoneService {
  public baseUrl: string = baseUrl

  public buildQuery = (params: object): string => EntityService.addQueryParams(baseUrl, params)
  public list = async (url: string): Promise<EntityArrayResponseType> => EntityService.list(url)
  public find = (login: string): Promise<EntityResponseType> => EntityService.find(baseUrl, login)
  public save = (data: object): Promise<EntityResponseType> => EntityService.save(baseUrl, data)
  public update = (data: object, id: number): Promise<EntityResponseType> => EntityService.update(`${baseUrl}/${id}`, data)
  public patch = (data: IDeliveryZone): Promise<EntityResponseType> => EntityService.patch(`${baseUrl}/${data.id}`, data)
  public remove = async (id: number): Promise<IResponse<void>> => EntityService.remove(baseUrl, id)
}

export default new DeliveryZoneService()
