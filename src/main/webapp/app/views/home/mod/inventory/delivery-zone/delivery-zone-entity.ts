import { IAction, IEntity } from '@/views/entities/common/entity-interface'
import appConstants from '@/shared/const/app-constants'
import { buildAddAction, buildRemoveAction, buildUpdateAction } from '@/views/entities/common/entity-api'

const entityRouteName = 'delivery-zones'

const entity: IEntity = {
  name: entityRouteName,
  classHumanized: 'deliveryZone.detail.title',
  title: 'deliveryZone.home.title',
  headerLabel: 'deliveryZone.home.title',
  searchPlaceHolder: 'deliveryZone.home.search',
  serverPaging: false,
  first: 0,
  pageSize: appConstants.APP.PAGE_MAX_SAFE_SIZE,
  propOrder: 'id',
  exportCSV: true,
  deleteMsgField: (item: any) => item.name,
  fields: [
    {
      id: 'code',
      header: 'deliveryZone.code',
      sortable: true,
    },
    {
      id: 'name',
      header: 'deliveryZone.name',
      sortable: true,
    },
  ],
  leftToolsActions: [buildAddAction(entityRouteName, 'deliveryZone.home.createLabel')],
  rowActions: [
    {
      ...buildUpdateAction(entityRouteName),
    },
    {
      ...buildRemoveAction(entityRouteName),
    },
  ],
}

export const deliveryZoneRouteName: string = entity.name
export const deliveryZoneEntity: IEntity = entity
export const deliveryZoneRoutes: IAction[] = entity.rowActions ? entity.rowActions : []
export default entity
