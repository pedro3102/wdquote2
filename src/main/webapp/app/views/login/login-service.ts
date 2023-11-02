import EntityService from '@/views/entities/common/entity-service'
import { IResponse } from '@/shared/api/http/request'
import { ILogin, ILoginResponse } from '@/views/login/login-model'
import { IUser } from '@/views/home/admin/user-management/user.model'

const apiPath = `${EntityService.apiPath}`

const loginUrl = `${apiPath}/authenticate`
// const logoutUrl = `${apiPath}/logout`;
const accountUrl = `${apiPath}/account`

type LoginResponseType = IResponse<ILoginResponse>
type EntityUserResponseType = IResponse<IUser>

class LoginService {
  public login = (loginData: ILogin): Promise<LoginResponseType> => EntityService.post(loginUrl, loginData)
  // public logout = (): Promise<any> => EntityService.post(logoutUrl);
  public getCurrentAccount = (): Promise<EntityUserResponseType> => EntityService.get(accountUrl)
}

export default new LoginService()
