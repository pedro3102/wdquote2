import {IResponse} from '@/shared/api/http/request'
import EntityService from '@/views/entities/common/entity-service'
import {locationRouteName} from '@/views/home/mod/inventory/location/location-entity'
import {ILocation} from '@/views/home/mod/inventory/location/location-model'

const baseUrl = `${EntityService.apiPath}/${locationRouteName}`

type EntityResponseType = IResponse<ILocation>
type EntityArrayResponseType = IResponse<ILocation[]>

class LocationService {
  public baseUrl: string = baseUrl

  public buildQuery = (params: object): string => EntityService.addQueryParams(baseUrl, params)
  public list = async (url: string): Promise<EntityArrayResponseType> => EntityService.list(url)
  public listBasic = async () => EntityService.get(baseUrl + '/basic')
  public find = (login: string): Promise<EntityResponseType> => EntityService.find(baseUrl, login)
  public save = (data: object): Promise<EntityResponseType> => EntityService.save(baseUrl, data)
  public update = (data: object, id: number): Promise<EntityResponseType> => EntityService.update(`${baseUrl}/${id}`, data)
  public patch = (data: ILocation): Promise<EntityResponseType> => EntityService.patch(`${baseUrl}/${data.id}`, data)
  public remove = async (id: number): Promise<IResponse<void>> => EntityService.remove(baseUrl, id)
}

export default new LocationService()
