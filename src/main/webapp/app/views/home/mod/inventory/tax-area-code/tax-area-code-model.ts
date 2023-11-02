export interface ITaxAreaCode {
  id?: any
  code?: string
  name?: string
}

export class TaxAreaCode implements ITaxAreaCode {
  constructor(public id?: any, public code?: string, public name?: string) {}
}
