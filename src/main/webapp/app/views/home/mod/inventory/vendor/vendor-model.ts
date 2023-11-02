export interface IVendor {
  id?: any
  code?: string
  name?: string
  codeName?: string,
  address?: string
}

export class Vendor implements IVendor {
  constructor(public id?: any, public code?: string, public name?: string, public address?: string) {
  }
}
