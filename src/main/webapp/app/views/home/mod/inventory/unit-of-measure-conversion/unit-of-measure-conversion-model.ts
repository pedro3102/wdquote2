import { IUnitOfMeasure } from '@/views/home/mod/inventory/unit-of-measure/unit-of-measure-model'

export interface IUnitOfMeasureConversion {
  id?: number
  conversionFactor?: number
  uom?: IUnitOfMeasure
  uomEquivalent?: IUnitOfMeasure
}

export class UnitOfMeasureConversion implements IUnitOfMeasureConversion {
  constructor(public id?: number, public conversionFactor?: number, public uom?: IUnitOfMeasure, public uomEquivalent?: IUnitOfMeasure) {}
}
