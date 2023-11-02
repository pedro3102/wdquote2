import { IAction, IEntity } from '@/views/entities/common/entity-interface'
import appConstants from '@/shared/const/app-constants'
import { buildAddAction, buildRemoveAction, buildUpdateAction } from '@/views/entities/common/entity-api'
import { TextAlign } from '@/shared/const/entity-constants'

const entityRouteName = 'languages'

const entity: IEntity = {
  name: entityRouteName,
  classHumanized: 'language.detail.title',
  headerLabel: 'language.home.title',
  title: 'language.home.title',
  searchPlaceHolder: 'language.home.search',
  serverPaging: false,
  first: 0,
  pageSize: appConstants.APP.PAGE_SIZE,
  reverse: false,
  propOrder: 'code',
  exportCSV: true,
  deleteMsgField: (item: any) => item.name,
  fields: [
    {
      id: 'code',
      header: 'language.code',
      sortable: true,
      textAlign: TextAlign.CENTER,
    },
    {
      id: 'name',
      header: 'language.name',
      sortable: true,
    },
  ],
  leftToolsActions: [buildAddAction(entityRouteName, 'language.home.createLabel')],
  rowActions: [
    {
      ...buildUpdateAction(entityRouteName),
    },
    {
      ...buildRemoveAction(entityRouteName),
    },
  ],
}

export const languageRouteName: string = entity.name
export const languageEntity: IEntity = entity
export const languageRoutes: IAction[] = entity.rowActions ? entity.rowActions : []
export default entity
