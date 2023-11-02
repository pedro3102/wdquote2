import { ILocation } from '@/views/home/mod/inventory/location/location-model'
import { IProduct } from '@/views/home/mod/inventory/product/product-model'
import { StockStatus } from '@/shared/const/entity-constants'

export interface IInventory {
  id?: number
  qty?: number
  unitCost?: number
  lastActivityDate?: Date
  shelf?: string | null
  reorderPoint?: number | null
  stockStatus?: StockStatus | null
  vendorLeadTime?: number | null
  location?: ILocation
  product?: IProduct
}

export class Inventory implements IInventory {
  constructor(
    public id?: number,
    public qty?: number,
    public unitCost?: number,
    public lastActivityDate?: Date,
    public shelf?: string | null,
    public reorderPoint?: number | null,
    public stockStatus?: StockStatus | null,
    public vendorLeadTime?: number | null,
    public location?: ILocation,
    public product?: IProduct
  ) {}
}
