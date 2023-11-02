export interface IDeliveryZone {
  id?: any
  code?: string
  name?: string
}

export class DeliveryZone implements IDeliveryZone {
  constructor(public id?: any, public code?: string, public name?: string) {}
}
