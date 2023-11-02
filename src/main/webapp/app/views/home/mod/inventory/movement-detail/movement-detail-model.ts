import { IMovement } from '@/views/home/mod/inventory/movement/movement-model'
import { IProduct } from '@/views/home/mod/inventory/product/product-model'
import { IStockPosition } from '@/views/home/mod/inventory/stock-position/stock-position-model'
import { IInventory } from '@/views/home/mod/inventory/inventory/inventory-model'

export interface IMovementDetail {
  id?: number
  movement?: IMovement
  product?: IProduct
  unitCost?: number
  qty?: number
  salePrice?: number
  vendorCode?: string
  inventoryQty?: number
  stockPosition?: IStockPosition
  inventory?: IInventory
}

export class MovementDetail implements IMovementDetail {
  constructor(
    public id?: number,
    public movement?: IMovement,
    public product?: IProduct,
    public unitCost: number = 0,
    public qty: number = 0,
    public salePrice: number = 0,
    public vendorCode?: string,
    public inventoryQty: number = 0,
    public stockPosition?: IStockPosition,
    public inventory?: IInventory
  ) {}
}
