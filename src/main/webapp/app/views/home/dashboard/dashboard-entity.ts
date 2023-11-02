import { IAction, IEntity } from '@/views/entities/common/entity-interface'
import appConstants from '@/shared/const/app-constants'

const entityRouteName = 'dashboard'

const entity: IEntity = {
  name: entityRouteName,
  classHumanized: 'dashboard.detail.title',
  headerLabel: 'dashboard.home.title',
  title: 'dashboard.home.title',
  searchPlaceHolder: 'dashboard.home.search',
  serverPaging: false,
  pageSize: appConstants.APP.PAGE_SIZE,
  reverse: true,
  propOrder: 'id',
  fields: [
    {
      id: 'name',
      header: 'dashboard.name',
    },
  ],
}

export const dashBoardRouteName: string = entity.name
export const dashBoardEntity: IEntity = entity
export const dashBoardRoutes: IAction[] = entity.rowActions ? entity.rowActions : []
export default entity
