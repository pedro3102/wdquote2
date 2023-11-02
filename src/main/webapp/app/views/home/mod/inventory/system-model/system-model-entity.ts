import { IAction, IEntity } from '@/views/entities/common/entity-interface'
import appConstants from '@/shared/const/app-constants'
import { buildAddAction, buildRemoveAction, buildUpdateAction } from '@/views/entities/common/entity-api'
import { FilterDisplayType, FilterMatchMode, FilterOperatorOptions, FilterType } from '@/shared/const/entity-constants'
import { ISystemModel } from '@/views/home/mod/inventory/system-model/system-model-model'

const entityRouteName = 'system-models'

const entity: IEntity = {
  name: entityRouteName,
  classHumanized: 'systemModel.detail.title',
  headerLabel: 'systemModel.home.title',
  title: 'systemModel.home.title',
  searchPlaceHolder: 'systemModel.home.search',
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
      header: 'systemModel.name',
      sortable: true,
      filterType: FilterType.TEXT,
    },
    {
      id: 'description',
      header: 'systemModel.description',
      sortable: true,
      filterType: FilterType.TEXT,
    },
    {
      id: 'system',
      header: 'systemModel.system',
      sortable: true,
      render: (systemModel: ISystemModel) => systemModel.system.name,
      filterType: FilterType.TEXT,
    },
  ],
  filters: {
    name: { operator: FilterOperatorOptions.AND, constraints: [{ value: null, matchMode: FilterMatchMode.CONTAINS }] },
    description: {
      operator: FilterOperatorOptions.AND,
      constraints: [{ value: null, matchMode: FilterMatchMode.CONTAINS }],
    },
    system: { operator: FilterOperatorOptions.AND, constraints: [{ value: null, matchMode: FilterMatchMode.EQUALS }] },
  },
  leftToolsActions: [buildAddAction(entityRouteName, 'systemModel.home.createLabel')],
  rowActions: [
    {
      ...buildUpdateAction(entityRouteName),
    },
    {
      ...buildRemoveAction(entityRouteName),
    },
  ],
}

export const systemModelRouteName: string = entity.name
export const systemModelEntity: IEntity = entity
export const systemModelRoutes: IAction[] = entity.rowActions ? entity.rowActions : []
export default entity
