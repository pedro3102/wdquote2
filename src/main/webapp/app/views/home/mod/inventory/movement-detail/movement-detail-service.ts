import {IResponse} from '@/shared/api/http/request'
import EntityService from '@/views/entities/common/entity-service'
import {movementDetailRouteName} from '@/views/home/mod/inventory/movement-detail/movement-detail-entity'
import {IMovement} from '@/views/home/mod/inventory/movement/movement-model'

const baseUrl = `${EntityService.apiPath}/${movementDetailRouteName}`

type EntityResponseType = IResponse<IMovement>
type EntityArrayResponseType = IResponse<IMovement[]>

class MovementDetailService {
  public baseUrl: string = baseUrl

  public buildQuery = (params: object): string => EntityService.addQueryParams(baseUrl, params)
  public list = async (url: string): Promise<EntityArrayResponseType> => EntityService.list(url)
  public find = (id: number): Promise<EntityResponseType> => EntityService.find(baseUrl, id)
  public save = (data: object): Promise<EntityResponseType> => EntityService.save(baseUrl, data)
  public update = (data: object, id: number): Promise<EntityResponseType> => EntityService.update(`${baseUrl}/${id}`, data)
  public patch = (data: IMovement): Promise<EntityResponseType> => EntityService.patch(`${baseUrl}/${data.id}`, data)
  public remove = async (id: number): Promise<IResponse<void>> => EntityService.remove(baseUrl, id)
}

export default new MovementDetailService()
