export interface ILoginResponse {
  // eslint-disable-next-line camelcase
  access_token: string
  companyId: number
  // eslint-disable-next-line camelcase
  expires_in: number
  iat: number
  id: number
  jti: string
  mfa: boolean
  // eslint-disable-next-line camelcase
  refresh_token: string
  scope: string
  // eslint-disable-next-line camelcase
  token_type: string
}

export interface ILogin {
  username?: string
  password?: string
  rememberMe?: boolean
}

export class Login implements ILogin {
  constructor(public username?: string, public password?: string, public rememberMe?: boolean) {}
}
