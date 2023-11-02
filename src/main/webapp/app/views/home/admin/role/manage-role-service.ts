import { IResponse } from '@/shared/api/http/request'
import { roleRouteName } from './role-entity'
import EntityService from '@/views/entities/common/entity-service'
import { IRole } from '@/views/home/admin/role/role-model'

const baseUrl = `${EntityService.apiPath}/admin/${roleRouteName}`

type EntityResponseType = IResponse<IRole>
type EntityArrayResponseType = IResponse<IRole[]>

class ManageRoleService {
  public buildQuery = (params: object) => EntityService.addQueryParams(baseUrl, params)

  public list = async (url: string = baseUrl): Promise<EntityArrayResponseType> => EntityService.list(url)

  public save = (data: object): Promise<EntityResponseType> => EntityService.save(baseUrl, data)

  public patch = (id: number, data: object): Promise<EntityResponseType> => EntityService.patch(`${baseUrl}/${id}`, data)

  public find = async (id: number): Promise<EntityResponseType> => EntityService.find(baseUrl, id)

  public remove = async (id: number): Promise<IResponse<void>> => EntityService.remove(baseUrl, id)
}

export default new ManageRoleService()
