import { IAction, IEntity } from '@/views/entities/common/entity-interface'
import appConstants from '@/shared/const/app-constants'
import { buildAddAction, buildRemoveAction, buildUpdateAction } from '@/views/entities/common/entity-api'

const entityRouteName = 'systems'

const entity: IEntity = {
  name: entityRouteName,
  classHumanized: 'system.detail.title',
  headerLabel: 'system.home.title',
  title: 'system.home.title',
  searchPlaceHolder: 'system.home.search',
  serverPaging: false,
  first: 0,
  pageSize: appConstants.APP.PAGE_SIZE,
  reverse: false,
  propOrder: 'name',
  exportCSV: true,
  deleteMsgField: (item: any) => item.name,
  fields: [
    {
      id: 'name',
      header: 'system.name',
      sortable: true,
    },
  ],
  leftToolsActions: [buildAddAction(entityRouteName, 'system.home.createLabel')],
  rowActions: [
    {
      ...buildUpdateAction(entityRouteName),
    },
    {
      ...buildRemoveAction(entityRouteName),
    },
  ],
}

export const systemRouteName: string = entity.name
export const systemEntity: IEntity = entity
export const systemRoutes: IAction[] = entity.rowActions ? entity.rowActions : []
export default entity
