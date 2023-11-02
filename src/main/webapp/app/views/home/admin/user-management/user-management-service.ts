import { IResponse } from '@/shared/api/http/request'
import { IUser } from '@/views/home/admin/user-management/user.model'
import EntityService from '@/views/entities/common/entity-service'

const baseUrl = `${EntityService.apiPath}/admin/users`

type EntityResponseType = IResponse<IUser>
type EntityArrayResponseType = IResponse<IUser[]>

class UserManagementService {
  public baseUrl: string = baseUrl

  public buildQuery = (params: object): string => EntityService.addQueryParams(baseUrl, params)
  public list = async (url: string): Promise<EntityArrayResponseType> => EntityService.list(url)
  public find = (login: string): Promise<EntityResponseType> => EntityService.find(baseUrl, login)
  public retrieveAuthorities = (): Promise<EntityResponseType> => EntityService.get('api/authorities')
  public changeStatus = (params): Promise<EntityResponseType> => EntityService.get(`${baseUrl}/change-active`, { params })
  public save = (data: object): Promise<EntityResponseType> => EntityService.save(baseUrl, data)
  public update = (data: object): Promise<EntityResponseType> => EntityService.update(baseUrl, data)
  public patch = (data: IUser): Promise<EntityResponseType> => EntityService.patch(`${baseUrl}/${data.id}`, data)
  public remove = async (id: number): Promise<IResponse<void>> => EntityService.remove(baseUrl, id)
}

export default new UserManagementService()
