import { IAction, IEntity } from '@/views/entities/common/entity-interface'
import appConstants from '@/shared/const/app-constants'
import { buildAddAction, buildRemoveAction, buildUpdateAction } from '@/views/entities/common/entity-api'
import customRender from '@/shared/renders/custom-render'
import { IMovementType } from '@/views/home/mod/inventory/movement-type/movement-type-model'
import { TextAlign } from '@/shared/const/entity-constants'

const entityRouteName = 'movement-types'

const entity: IEntity = {
  name: entityRouteName,
  classHumanized: 'movementType.detail.title',
  headerLabel: 'movementType.home.title',
  title: 'movementType.home.title',
  searchPlaceHolder: 'movementType.home.search',
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
      header: 'movementType.code',
      sortable: true,
      textAlign: TextAlign.CENTER,
    },
    {
      id: 'name',
      header: 'movementType.name',
      sortable: true,
    },
    {
      id: 'type',
      header: 'movementType.operationType',
      sortable: true,
      render: (movementType: IMovementType) => customRender.operationType(movementType.type),
      textAlign: TextAlign.CENTER,
    },
    {
      id: 'counterpart',
      header: 'movementType.counterpart',
      sortable: true,
      render: (movementType: IMovementType) => customRender.operationCounterpart(movementType.counterpart),
      textAlign: TextAlign.CENTER,
    },
    {
      id: 'oppositeMovementType',
      header: 'movementType.oppositeMovementType',
      sortable: true,
      render: (movementType: IMovementType) =>
        movementType.oppositeMovementType != null
          ? movementType.oppositeMovementType.code + '-' + movementType.oppositeMovementType.name
          : '',
      textAlign: TextAlign.CENTER,
    },
  ],
  leftToolsActions: [buildAddAction(entityRouteName, 'movementType.home.createLabel')],
  rowActions: [
    {
      ...buildUpdateAction(entityRouteName),
    },
    {
      ...buildRemoveAction(entityRouteName),
    },
  ],
}

export const movementTypeRouteName: string = entity.name
export const movementTypeEntity: IEntity = entity
export const movementTypeRoutes: IAction[] = entity.rowActions ? entity.rowActions : []
export default entity
