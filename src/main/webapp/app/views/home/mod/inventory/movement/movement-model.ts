import { IDTExpanded } from '@/views/entities/common/entity-interface'
import { MovementStatus, OperationCounterpart, OperationType } from '@/shared/const/entity-constants'
import { IMovementType } from '@/views/home/mod/inventory/movement-type/movement-type-model'
import { ILocation } from '@/views/home/mod/inventory/location/location-model'
import { IVendor } from '@/views/home/mod/inventory/vendor/vendor-model'
import { ICustomer } from '@/views/home/mod/inventory/customer/customer-model'

export interface IMovement extends IDTExpanded {
  id?: number
  no?: string
  reference?: string
  date?: Date
  note?: string
  canceledDate?: Date
  status?: MovementStatus
  movementType?: IMovementType
  location?: ILocation
  type?: OperationType
  counterpart?: OperationCounterpart
  counterpartEntity?: any
  counterpartLocation?: ILocation
  counterpartVendor?: IVendor
  counterpartCustomer?: ICustomer
  consecutive?: number
}

export class Movement implements IMovement {
  constructor(
    public id?: number,
    public no: string = '?',
    public reference?: string,
    public date: Date = new Date(),
    public note?: string,
    public canceledDate?: Date,
    public status: MovementStatus = MovementStatus.PENDING,
    public movementType?: IMovementType,
    public location?: ILocation,
    public type?: OperationType,
    public counterpart?: OperationCounterpart,
    public counterpartLocation?: ILocation,
    public counterpartVendor?: IVendor,
    public counterpartCustomer?: ICustomer,
    public consecutive: number = 0
  ) {}
}
