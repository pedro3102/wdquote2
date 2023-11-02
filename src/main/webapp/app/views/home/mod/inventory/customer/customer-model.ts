import { IDeliveryZone } from '@/views/home/mod/inventory/delivery-zone/delivery-zone-model'
import { ITaxAreaCode } from '@/views/home/mod/inventory/tax-area-code/tax-area-code-model'
import { IDTExpanded } from '@/views/entities/common/entity-interface'

export interface ICustomer extends IDTExpanded {
  id?: number
  code?: string
  name?: string
  codeName?: string,
  address?: string | null
  taxLiable?: boolean
  taxExemptionCode?: string | null
  paymentTerms?: string | null
  creditLimit?: number | null
  blocked?: boolean
  deliveryZone?: IDeliveryZone
  taxAreaCode?: ITaxAreaCode
}

export class Customer implements ICustomer {
  constructor(
    public id?: number,
    public code?: string,
    public name?: string,
    public address?: string | null,
    public taxLiable?: boolean,
    public taxExemptionCode?: string | null,
    public paymentTerms?: string | null,
    public creditLimit?: number | null,
    public blocked?: boolean,
    public deliveryZone?: IDeliveryZone,
    public taxAreaCode?: ITaxAreaCode
  ) {
    this.taxLiable = this.taxLiable ?? false
    this.blocked = this.blocked ?? false
  }
}
