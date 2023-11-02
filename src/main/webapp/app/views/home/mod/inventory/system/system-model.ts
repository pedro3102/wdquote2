export interface ISystem {
  id?: number
  name?: string
  picture?: string
}

export class System implements ISystem {
  constructor(public id?: number, public name?: string, public picture?: string) {}
}
