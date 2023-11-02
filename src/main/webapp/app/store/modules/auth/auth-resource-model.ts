import { IAuthAction } from '@/store/modules/auth/auth-action-model'

export interface IAuthResource {
  name?: string
  actions?: IAuthAction[]
}

export class AuthResource implements IAuthResource {
  constructor(public name?: string, public actions?: IAuthAction[]) {
    this.actions = []
  }
}
