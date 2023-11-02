export interface IMenuItem {
  id?: number | string
  open?: boolean
  visible?: boolean
  separator?: boolean
  label?: string
  icon?: string
  rightIcon?: string
  style?: string
  to?: string
  url?: string
  access?: string
  class?: string
  target?: string
  items?: IMenuItem[]
}

export interface IMenu {
  menu: IMenuItem[]
}
