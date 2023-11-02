import { ISystem } from '@/views/home/mod/inventory/system/system-model'

export interface ISystemModel {
  id?: number
  name?: string
  description?: string
  picture?: string | null
  system?: ISystem
}

export class SystemModel implements ISystemModel {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string,
    public picture?: string | null,
    public system?: ISystem
  ) {}
}
