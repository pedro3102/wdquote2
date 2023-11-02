import { IAction, IEntity } from '@/views/entities/common/entity-interface'
import appConstants from '@/shared/const/app-constants'
import { buildAddAction, buildRemoveAction, buildUpdateAction } from '@/views/entities/common/entity-api'

const entityRouteName = 'tax-area-codes'

const entity: IEntity = {
  name: entityRouteName,
  classHumanized: 'taxAreaCode.detail.title',
  title: 'taxAreaCode.home.title',
  headerLabel: 'taxAreaCode.home.title',
  searchPlaceHolder: 'taxAreaCode.home.search',
  serverPaging: false,
  first: 0,
  pageSize: appConstants.APP.PAGE_MAX_SAFE_SIZE,
  propOrder: 'id',
  exportCSV: true,
  deleteMsgField: (item: any) => item.name,
  fields: [
    {
      id: 'code',
      header: 'taxAreaCode.code',
      sortable: true,
    },
    {
      id: 'name',
      header: 'taxAreaCode.name',
      sortable: true,
    },
  ],
  leftToolsActions: [buildAddAction(entityRouteName, 'taxAreaCode.home.createLabel')],
  rowActions: [
    {
      ...buildUpdateAction(entityRouteName),
    },
    {
      ...buildRemoveAction(entityRouteName),
    },
  ],
}

export const taxAreaCodeRouteName: string = entity.name
export const taxAreaCodeEntity: IEntity = entity
export const taxAreaCodeRoutes: IAction[] = entity.rowActions ? entity.rowActions : []
export default entity
