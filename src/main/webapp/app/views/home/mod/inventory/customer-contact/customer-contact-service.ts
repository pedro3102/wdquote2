import { IResponse } from '@/shared/api/http/request'
import EntityService from '@/views/entities/common/entity-service'
import { ICustomerContact } from '@/views/home/mod/inventory/customer-contact/customer-contact-model'
import { customerContactRouteName } from '@/views/home/mod/inventory/customer-contact/customer-contact-entity'

const baseUrl = `${EntityService.apiPath}/${customerContactRouteName}`

type EntityResponseType = IResponse<ICustomerContact>
type EntityArrayResponseType = IResponse<ICustomerContact[]>

class CustomerContactService {
  public baseUrl: string = baseUrl

  public buildQuery = (params: object): string => EntityService.addQueryParams(baseUrl, params)
  public list = async (url: string): Promise<EntityArrayResponseType> => EntityService.list(url)
  public find = (id: number): Promise<EntityResponseType> => EntityService.find(baseUrl, id)
  public save = (data: object): Promise<EntityResponseType> => EntityService.save(baseUrl, data)
  public update = (data: object, id: number): Promise<EntityResponseType> => EntityService.update(`${baseUrl}/${id}`, data)
  public patch = (data: ICustomerContact): Promise<EntityResponseType> => EntityService.patch(`${baseUrl}/${data.id}`, data)
  public remove = async (id: number): Promise<IResponse<void>> => EntityService.remove(baseUrl, id)
}

export default new CustomerContactService()
