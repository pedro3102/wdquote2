import {IResponse} from '@/shared/api/http/request'
import EntityService from '@/views/entities/common/entity-service'
import {movementTypeRouteName} from '@/views/home/mod/inventory/movement-type/movement-type-entity'
import {IMovementType} from '@/views/home/mod/inventory/movement-type/movement-type-model'
import {OperationType} from "@/shared/const/entity-constants";

const baseUrl = `${EntityService.apiPath}/${movementTypeRouteName}`

type EntityResponseType = IResponse<IMovementType>
type EntityArrayResponseType = IResponse<IMovementType[]>

class MovementTypeService {
  public baseUrl: string = baseUrl

  public buildQuery = (params: object): string => EntityService.addQueryParams(baseUrl, params)
  public list = async (url: string): Promise<EntityArrayResponseType> => EntityService.list(url)
  public listBasic = async (type?: OperationType) => EntityService.get(EntityService.addQueryParams(baseUrl + "/basic", {type: type ? type : ""}))
  public find = (id: number): Promise<EntityResponseType> => EntityService.find(baseUrl, id)
  public save = (data: object): Promise<EntityResponseType> => EntityService.save(baseUrl, data)
  public update = (data: object, id: number): Promise<EntityResponseType> => EntityService.update(`${baseUrl}/${id}`, data)
  public patch = (data: IMovementType): Promise<EntityResponseType> => EntityService.patch(`${baseUrl}/${data.id}`, data)
  public remove = async (id: number): Promise<IResponse<void>> => EntityService.remove(baseUrl, id)
}

export default new MovementTypeService()
