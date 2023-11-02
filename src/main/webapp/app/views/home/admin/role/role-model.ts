export interface IRole {
  id?: number
  name?: string
  description?: string
}

export class Role implements IRole {
  constructor(public id?: number, public name?: string, public description?: string) {}
}
