import { IResponse } from '@/shared/api/http/request'
import EntityService from '@/views/entities/common/entity-service'
import { systemModelRouteName } from '@/views/home/mod/inventory/system-model/system-model-entity'
import { ISystemModel } from '@/views/home/mod/inventory/system-model/system-model-model'

const baseUrl = `${EntityService.apiPath}/${systemModelRouteName}`

type EntityResponseType = IResponse<ISystemModel>
type EntityArrayResponseType = IResponse<ISystemModel[]>

class SystemModelService {
  public baseUrl: string = baseUrl

  public buildQuery = (params: object): string => EntityService.addQueryParams(baseUrl, params)
  public list = async (url: string): Promise<EntityArrayResponseType> => EntityService.list(url)
  public listBasic = async () => EntityService.get(baseUrl + '/basic')
  public find = (id: number): Promise<EntityResponseType> => EntityService.find(baseUrl, id)
  public save = (data: object): Promise<EntityResponseType> => EntityService.save(baseUrl, data)
  public update = (data: object, id: number): Promise<EntityResponseType> => EntityService.update(`${baseUrl}/${id}`, data)
  public patch = (data: ISystemModel): Promise<EntityResponseType> => EntityService.patch(`${baseUrl}/${data.id}`, data)
  public remove = async (id: number): Promise<IResponse<void>> => EntityService.remove(baseUrl, id)
}

export default new SystemModelService()
