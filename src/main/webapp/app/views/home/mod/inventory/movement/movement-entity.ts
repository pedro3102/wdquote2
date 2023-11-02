import { IAction, IEntity } from '@/views/entities/common/entity-interface'
import appConstants from '@/shared/const/app-constants'
import { buildAddAction, buildRemoveAction, buildUpdateAction } from '@/views/entities/common/entity-api'
import {
  ActionType,
  BasicAction,
  FilterDisplayType,
  FilterMatchMode,
  FilterOperatorOptions,
  FilterType,
  OperationCounterpart,
  SeverityCodes,
  TextAlign,
} from '@/shared/const/entity-constants'
import { IMovement } from '@/views/home/mod/inventory/movement/movement-model'
import TranslationService from '@/locale/translation-service'
import { movementDetailEntity } from '@/views/home/mod/inventory/movement-detail/movement-detail-entity'
import dayjs from 'dayjs'
import { DATE_FORMAT_USA } from '@/shared/composables/date-format'
import { buildRemoveRouteName } from '@/shared/config/router/router-utils'

const entityRouteName = 'movements'

const buildConfirmAction = (entityRouteName: string): IAction => ({
  id: BasicAction.CONFIRM,
  path: 'confirm',
  name: buildRemoveRouteName(entityRouteName),
  type: ActionType.BUTTON,
  label: () => 'entity.action.confirm',
  title: () => 'entity.action.confirm',
  class: () => '',
  icon: () => 'check-double',
  iconColor: () => SeverityCodes.SUCCESS,
  visible: (): boolean => true,
  execute: (row?: object, emit?) => emit('custom', row),
})

const entity: IEntity = {
  name: entityRouteName,
  classHumanized: 'movement.detail.title',
  title: 'movement.home.title',
  headerLabel: 'movement.home.title',
  searchPlaceHolder: 'movement.home.search',
  serverPaging: true,
  first: 0,
  pageSize: appConstants.APP.PAGE_MAX_SAFE_SIZE,
  reverse: false,
  propOrder: 'id',
  exportCSV: true,
  filterDisplay: FilterDisplayType.MENU,
  dtMaximizableModal: true,
  deleteMsgField: (item: any) => item.name,
  fields: [
    {
      id: 'no',
      header: 'movement.no',
      sortable: true,
      filterType: FilterType.TEXT,
      textAlign: TextAlign.CENTER,
    },
    {
      id: 'movementType',
      header: 'movement.movementType',
      sortable: true,
      render: (movement: IMovement) => movement.movementType.code,
      filterType: FilterType.TEXT,
      sortField: 'movementType.code',
      textAlign: TextAlign.CENTER,
    },
    {
      id: 'reference',
      header: 'movement.reference',
      sortable: true,
      filterType: FilterType.TEXT,
    },
    {
      id: 'date',
      header: 'movement.date',
      sortable: true,
      render: (movement: IMovement) => dayjs(movement.date).format(DATE_FORMAT_USA),
      filterType: FilterType.DATE,
      textAlign: TextAlign.CENTER,
    },
    {
      id: 'location',
      header: 'movement.location',
      sortable: true,
      render: (movement: IMovement) => movement.location.code,
      filterType: FilterType.TEXT,
      sortField: 'location.code',
      textAlign: TextAlign.CENTER,
    },
    {
      id: 'counterpart',
      header: 'movement.counterpart',
      sortable: false,
      render: (movement: IMovement) =>
        movement.counterpart == OperationCounterpart.VENDOR
          ? movement.counterpartVendor && movement.counterpartVendor.code
          : movement.counterpart == OperationCounterpart.CUSTOMER
          ? movement.counterpartCustomer && movement.counterpartCustomer.code
          : movement.counterpart == OperationCounterpart.LOCATION
          ? movement.counterpartLocation && movement.counterpartLocation.code
          : '',
      textAlign: TextAlign.CENTER,
      filterType: FilterType.TEXT,
    },
    {
      id: 'note',
      header: 'movement.note',
      sortable: true,
      filterType: FilterType.TEXT,
    },
    {
      id: 'canceledDate',
      header: 'movement.canceledDate',
      sortable: true,
      render: (movement: IMovement) => movement.canceledDate && dayjs(movement.canceledDate).format(DATE_FORMAT_USA),
      filterType: FilterType.DATE,
      textAlign: TextAlign.CENTER,
    },
  ],
  filters: {
    no: { operator: FilterOperatorOptions.AND, constraints: [{ value: null, matchMode: FilterMatchMode.CONTAINS }] },
    movementType: {
      operator: FilterOperatorOptions.AND,
      constraints: [{ value: null, matchMode: FilterMatchMode.CONTAINS }],
    },
    reference: { operator: FilterOperatorOptions.AND, constraints: [{ value: null, matchMode: FilterMatchMode.CONTAINS }] },
    date: { operator: FilterOperatorOptions.AND, constraints: [{ value: null, matchMode: FilterMatchMode.CONTAINS }] },
    location: { operator: FilterOperatorOptions.AND, constraints: [{ value: null, matchMode: FilterMatchMode.CONTAINS }] },
    counterpart: {
      operator: FilterOperatorOptions.AND,
      constraints: [{ value: null, matchMode: FilterMatchMode.CONTAINS }],
    },
    note: { operator: FilterOperatorOptions.AND, constraints: [{ value: null, matchMode: FilterMatchMode.CONTAINS }] },
    canceledDate: {
      operator: FilterOperatorOptions.AND,
      constraints: [{ value: null, matchMode: FilterMatchMode.CONTAINS }],
    },
  },
  leftToolsActions: [buildAddAction(entityRouteName, 'movement.home.createLabel')],
  rowActions: [
    {
      ...buildUpdateAction(entityRouteName),
    },
    {
      ...buildConfirmAction(entityRouteName),
    },
    {
      ...buildRemoveAction(entityRouteName),
    },
  ],
  children: movementDetailEntity,
  childrenHeaderLabelRender: (row: IMovement) =>
    TranslationService.getInstanced().t('movementDetail.detail.childTitle', { movement: row.no }),
}

export const movementRouteName: string = entity.name
export const movementEntity: IEntity = entity
export const movementRoutes: IAction[] = entity.rowActions ? entity.rowActions : []
export default entity
