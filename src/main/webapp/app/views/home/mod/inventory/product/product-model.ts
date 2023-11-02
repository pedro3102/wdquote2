import { IProductCategory } from '@/views/home/mod/inventory/product-category/product-category-model'
import { ISystemModel } from '@/views/home/mod/inventory/system-model/system-model-model'
import { IUnitOfMeasure } from '@/views/home/mod/inventory/unit-of-measure/unit-of-measure-model'

export interface IProduct {
  id?: number
  code?: string
  description?: string
  weight?: number | null
  picture?: string | null
  uom?: IUnitOfMeasure
  uomWeight?: IUnitOfMeasure
  category?: IProductCategory
  systemModels?: ISystemModel[] | null
  qty?: number
}

export class Product implements IProduct {
  constructor(
    public id?: number,
    public code?: string,
    public description?: string,
    public weight?: number | null,
    public picture?: string | null,
    public uom?: IUnitOfMeasure,
    public uomWeight?: IUnitOfMeasure,
    public category?: IProductCategory,
    public systemModels?: ISystemModel[] | null
  ) {}
}
