export interface IAuthAction {
  name?: string
  roles?: string[]
}

export class AuthAction implements IAuthAction {
  constructor(public name?: string, public roles?: string[]) {
    this.roles = []
  }
}
