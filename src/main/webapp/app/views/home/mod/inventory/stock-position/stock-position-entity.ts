import {IAction, IEntity} from '@/views/entities/common/entity-interface'
import appConstants from '@/shared/const/app-constants'
import {buildAddAction, buildRemoveAction, buildUpdateAction} from '@/views/entities/common/entity-api'
import {FilterDisplayType, FilterMatchMode, FilterOperatorOptions, FilterType} from '@/shared/const/entity-constants'

const entityRouteName = 'stock-positions'

const entity: IEntity = {
  name: entityRouteName,
  classHumanized: 'stockPosition.detail.title',
  headerLabel: 'stockPosition.home.title',
  title: 'stockPosition.home.title',
  searchPlaceHolder: 'stockPosition.home.search',
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
      id: 'name',
      header: 'stockPosition.name',
      sortable: true,
      filterType: FilterType.TEXT,
    },
    {
      id: 'description',
      header: 'stockPosition.description',
      sortable: true,
      filterType: FilterType.TEXT,
    },
  ],
  filters: {
    name: {operator: FilterOperatorOptions.AND, constraints: [{value: null, matchMode: FilterMatchMode.CONTAINS}]},
    description: {
      operator: FilterOperatorOptions.AND,
      constraints: [{value: null, matchMode: FilterMatchMode.CONTAINS}],
    },
  },
  leftToolsActions: [buildAddAction(entityRouteName, 'stockPosition.home.createLabel')],
  rowActions: [
    {
      ...buildUpdateAction(entityRouteName),
    },
    {
      ...buildRemoveAction(entityRouteName),
    },
  ],
}

export const stockPositionRouteName: string = entity.name
export const stockPositionEntity: IEntity = entity
export const stockPositionRoutes: IAction[] = entity.rowActions ? entity.rowActions : []
export default entity
