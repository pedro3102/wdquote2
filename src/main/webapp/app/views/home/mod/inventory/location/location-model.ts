import {LocationType} from '@/shared/const/entity-constants'

export interface ILocation {
  id?: any
  code?: string
  name?: string
  codeName?: string;
  address?: string
  isWarehouse?: boolean
  locationType?: LocationType
}

export class Location implements ILocation {
  constructor(
    public id?: any,
    public code?: string,
    public name?: string,
    public address?: string,
    public email?: string,
    public isWarehouse?: boolean,
    public locationType?: LocationType
  ) {
  }
}
