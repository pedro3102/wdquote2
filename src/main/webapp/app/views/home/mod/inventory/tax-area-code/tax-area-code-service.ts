import { IResponse } from '@/shared/api/http/request'
import EntityService from '@/views/entities/common/entity-service'
import { taxAreaCodeRouteName } from '@/views/home/mod/inventory/tax-area-code/tax-area-code-entity'
import { ITaxAreaCode } from '@/views/home/mod/inventory/tax-area-code/tax-area-code-model'

const baseUrl = `${EntityService.apiPath}/${taxAreaCodeRouteName}`

type EntityResponseType = IResponse<ITaxAreaCode>
type EntityArrayResponseType = IResponse<ITaxAreaCode[]>

class TaxAreaCodeService {
  public baseUrl: string = baseUrl

  public buildQuery = (params: object): string => EntityService.addQueryParams(baseUrl, params)
  public list = async (url: string): Promise<EntityArrayResponseType> => EntityService.list(url)
  public find = (login: string): Promise<EntityResponseType> => EntityService.find(baseUrl, login)
  public save = (data: object): Promise<EntityResponseType> => EntityService.save(baseUrl, data)
  public update = (data: object, id: number): Promise<EntityResponseType> => EntityService.update(`${baseUrl}/${id}`, data)
  public patch = (data: ITaxAreaCode): Promise<EntityResponseType> => EntityService.patch(`${baseUrl}/${data.id}`, data)
  public remove = async (id: number): Promise<IResponse<void>> => EntityService.remove(baseUrl, id)
}

export default new TaxAreaCodeService()
