export interface IProductCategory {
  id?: number
  name?: string
  picture?: string
}

export class ProductCategory implements IProductCategory {
  constructor(public id?: number, public name?: string, public picture?: string) {}
}
