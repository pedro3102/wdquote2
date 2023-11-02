import { IAction, IEntity } from '@/views/entities/common/entity-interface'
import appConstants from '@/shared/const/app-constants'
import { buildAddAction, buildRemoveAction, buildUpdateAction } from '@/views/entities/common/entity-api'
import { FilterDisplayType, FilterMatchMode, FilterOperatorOptions } from '@/shared/const/entity-constants'

const entityRouteName = 'vendors'

const entity: IEntity = {
  name: entityRouteName,
  classHumanized: 'vendor.detail.title',
  title: 'vendor.home.title',
  headerLabel: 'vendor.home.title',
  searchPlaceHolder: 'vendor.home.search',
  serverPaging: true,
  first: 0,
  pageSize: appConstants.APP.PAGE_SIZE,
  reverse: false,
  propOrder: 'id',
  exportCSV: true,
  filterDisplay: FilterDisplayType.MENU,
  deleteMsgField: (item: any) => item.name,
  fields: [
    {
      id: 'id',
      header: 'vendor.id',
    },
    {
      id: 'code',
      header: 'vendor.code',
      sortable: true,
    },
    {
      id: 'name',
      header: 'vendor.name',
      sortable: true,
    },
    {
      id: 'address',
      header: 'vendor.address',
    },
  ],
  filters: {
    code: { operator: FilterOperatorOptions.AND, constraints: [{ value: null, matchMode: FilterMatchMode.CONTAINS }] },
    name: { operator: FilterOperatorOptions.AND, constraints: [{ value: null, matchMode: FilterMatchMode.CONTAINS }] },
  },
  leftToolsActions: [buildAddAction(entityRouteName, 'vendor.home.createLabel')],
  rowActions: [
    {
      ...buildUpdateAction(entityRouteName),
    },
    {
      ...buildRemoveAction(entityRouteName),
    },
  ],
}

export const vendorRouteName: string = entity.name
export const vendorEntity: IEntity = entity
export const vendorRoutes: IAction[] = entity.rowActions ? entity.rowActions : []
export default entity
