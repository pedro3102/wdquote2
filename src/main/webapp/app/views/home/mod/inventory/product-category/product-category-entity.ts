import { IAction, IEntity } from '@/views/entities/common/entity-interface'
import appConstants from '@/shared/const/app-constants'
import { buildAddAction, buildRemoveAction, buildUpdateAction } from '@/views/entities/common/entity-api'

const entityRouteName = 'product-categories'

const entity: IEntity = {
  name: entityRouteName,
  classHumanized: 'productCategory.detail.title',
  headerLabel: 'productCategory.home.title',
  title: 'productCategory.home.title',
  searchPlaceHolder: 'productCategory.home.search',
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
      header: 'productCategory.name',
      sortable: true,
    },
  ],
  leftToolsActions: [buildAddAction(entityRouteName, 'productCategory.home.createLabel')],
  rowActions: [
    {
      ...buildUpdateAction(entityRouteName),
    },
    {
      ...buildRemoveAction(entityRouteName),
    },
  ],
}

export const productCategoryRouteName: string = entity.name
export const productCategoryEntity: IEntity = entity
export const productCategoryRoutes: IAction[] = entity.rowActions ? entity.rowActions : []
export default entity
