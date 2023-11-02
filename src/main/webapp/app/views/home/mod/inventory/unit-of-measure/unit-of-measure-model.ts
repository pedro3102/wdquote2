import { IDTExpanded } from '@/views/entities/common/entity-interface'

export interface IUnitOfMeasure extends IDTExpanded {
  id?: number
  name?: string
  abbreviation?: string
  allowsDecimal?: boolean
}

export class UnitOfMeasure implements IUnitOfMeasure {
  constructor(public id?: number, public name?: string, public abbreviation?: string, public allowsDecimal?: boolean) {
    this.allowsDecimal = this.allowsDecimal ?? false
  }
}
