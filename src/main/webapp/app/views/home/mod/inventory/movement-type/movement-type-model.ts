import {OperationCounterpart, OperationType} from '@/shared/const/entity-constants'

export interface IMovementType {
  id?: number
  code?: string
  name?: string
  codeName?: string;
  type?: OperationType
  counterpart?: OperationCounterpart
  oppositeMovementType?: IMovementType
}

export class MovementType implements IMovementType {
  constructor(public id?: number, public code?: string, public name?: string,
              public type: OperationType = OperationType.IN,
              public counterpart: OperationCounterpart = OperationCounterpart.NONE,
              public oppositeMovementType?: IMovementType) {
  }
}
