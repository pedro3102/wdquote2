import { IResponse } from '@/shared/api/http/request'
import { roleRouteName } from './role-entity'
import { IRole } from '@/views/home/admin/role/role-model'
import { IAction } from '@/views/entities/common/entity-interface'
import EntityService from '@/views/entities/common/entity-service'

const baseUrl = `${EntityService.apiPath}/admin/${roleRouteName}`

type EntityResponseType = IResponse<IRole>
type EntityArrayResponseType = IResponse<IRole[]>
type ActionEntityArrayResponseType = IResponse<IAction[]>

class RoleService {
  public list = async (url: string = baseUrl): Promise<EntityArrayResponseType> => EntityService.list(url)

  public listChildren = async (id: number): Promise<ActionEntityArrayResponseType> => EntityService.list(`${baseUrl}/${id}/actions`)

  public toggleAction = (id: number, data: object | undefined): Promise<EntityResponseType> =>
    EntityService.update(`${baseUrl}/${id}/actions`, data)
}

export default new RoleService()
