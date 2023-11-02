import { IResponse } from '@/shared/api/http/request'
import EntityService from '@/views/entities/common/entity-service'
import { unitOfMeasureConversionRouteName } from '@/views/home/mod/inventory/unit-of-measure-conversion/unit-of-measure-conversion-entity'
import { IUnitOfMeasure } from '@/views/home/mod/inventory/unit-of-measure/unit-of-measure-model'

const baseUrl = `${EntityService.apiPath}/${unitOfMeasureConversionRouteName}`

type EntityResponseType = IResponse<IUnitOfMeasure>
type EntityArrayResponseType = IResponse<IUnitOfMeasure[]>

class UnitOfMeasureConversionService {
  public baseUrl: string = baseUrl

  public buildQuery = (params: object): string => EntityService.addQueryParams(baseUrl, params)
  public list = async (url: string): Promise<EntityArrayResponseType> => EntityService.list(url)
  public find = (id: number): Promise<EntityResponseType> => EntityService.find(baseUrl, id)
  public save = (data: object): Promise<EntityResponseType> => EntityService.save(baseUrl, data)
  public update = (data: object, id: number): Promise<EntityResponseType> => EntityService.update(`${baseUrl}/${id}`, data)
  public patch = (data: IUnitOfMeasure): Promise<EntityResponseType> => EntityService.patch(`${baseUrl}/${data.id}`, data)
  public remove = async (id: number): Promise<IResponse<void>> => EntityService.remove(baseUrl, id)
}

export default new UnitOfMeasureConversionService()
