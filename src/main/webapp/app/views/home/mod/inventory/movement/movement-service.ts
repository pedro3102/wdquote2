import { IResponse } from '@/shared/api/http/request'
import EntityService from '@/views/entities/common/entity-service'
import { movementRouteName } from '@/views/home/mod/inventory/movement/movement-entity'
import { IMovement } from '@/views/home/mod/inventory/movement/movement-model'

const baseUrl = `${EntityService.apiPath}/${movementRouteName}`

type EntityResponseType = IResponse<IMovement>
type EntityArrayResponseType = IResponse<IMovement[]>

class MovementService {
  public baseUrl: string = baseUrl

  public buildQuery = (params: object): string => EntityService.addQueryParams(baseUrl, params)
  public list = async (url: string): Promise<EntityArrayResponseType> => EntityService.list(url)
  public find = (id: number): Promise<EntityResponseType> => EntityService.find(baseUrl, id)
  public save = (data: object): Promise<EntityResponseType> => EntityService.save(baseUrl, data)
  public update = (data: object, id: number): Promise<EntityResponseType> => EntityService.update(`${baseUrl}/${id}`, data)
  public patch = (data: IMovement): Promise<EntityResponseType> => EntityService.patch(`${baseUrl}/${data.id}`, data)
  public remove = async (id: number): Promise<IResponse<void>> => EntityService.remove(baseUrl, id)
  public removeDetail = async (id: number): Promise<IResponse<void>> => EntityService.post(`${baseUrl}/delete-details/${id}`)
  public confirm = (id: number): Promise<EntityResponseType> => EntityService.put(`${baseUrl}/confirm/${id}`)
}

export default new MovementService()
