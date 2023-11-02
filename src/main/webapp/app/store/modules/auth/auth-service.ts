import { IResponse } from '@/shared/api/http/request'
import { IAuthResource } from '@/store/modules/auth/auth-resource-model'
import EntityService from '@/views/entities/common/entity-service'

const baseUrl = `${EntityService.apiPath}/authorization`

type EntityResponseType = IResponse<IAuthResource>

class AuthService {
  public loadResource = async (resource: string): Promise<EntityResponseType> => EntityService.get(`${baseUrl}/${resource}/resource`)
}

export default new AuthService()
