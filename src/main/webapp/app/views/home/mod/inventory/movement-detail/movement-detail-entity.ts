import { IAction, IEntity } from '@/views/entities/common/entity-interface'
import { FilterMatchMode, FilterOperatorOptions, FilterType, TextAlign } from '@/shared/const/entity-constants'
import { IMovementDetail } from '@/views/home/mod/inventory/movement-detail/movement-detail-model'
import { buildRemoveAction } from '@/views/entities/common/entity-api'
import TranslationService from '@/locale/translation-service'
import { IMovement } from '@/views/home/mod/inventory/movement/movement-model'

const entityRouteName = 'movement-details'

const entity: IEntity = {
  name: entityRouteName,
  classHumanized: 'movementDetail.detail.title',
  title: 'movementDetail.home.title',
  headerLabel: 'movementDetail.home.title',
  searchPlaceHolder: 'movementDetail.home.search',
  serverPaging: false,
  first: 0,
  propOrder: 'id',
  reverse: true,
  deleteMsgField: (item: any) => item.product.code,
  dtDisablePaginator: true,
  disableGlobalFilter: false,
  dtAddRowInPlace: true,
  dtEditRowInPlace: true,
  dtMaximizableModal: true,
  childrenHeaderLabelRender: (row: IMovement) =>
    TranslationService.getInstanced().t('movementDetail.detail.childTitle', { movement: row.no }),
  fields: [
    {
      id: 'product.code',
      header: 'product.code',
      sortable: true,
      render: (movementDetail: IMovementDetail) => movementDetail.product.code,
      filterType: FilterType.TEXT,
      sortField: 'product.code',
      textAlign: TextAlign.CENTER,
    },
    {
      id: 'product.description',
      header: 'product.description',
      render: (movementDetail: IMovementDetail) => movementDetail.product?.description,
      sortable: true,
      filterType: FilterType.TEXT,
      sortField: 'product.description',
    },
    {
      id: 'product.uom.abbreviation',
      header: 'product.uom',
      render: (movementDetail: IMovementDetail) => movementDetail.product?.uom?.abbreviation,
      sortable: true,
      filterType: FilterType.TEXT,
      sortField: 'product.uom.abbreviation',
      textAlign: TextAlign.CENTER,
    },
    {
      id: 'vendorCode',
      header: 'movementDetail.vendorCode',
      sortable: true,
      textAlign: TextAlign.CENTER,
    },
    {
      id: 'unitCost',
      header: 'movementDetail.unitCost',
      sortable: true,
      textAlign: TextAlign.END,
    },
    {
      id: 'salePrice',
      header: 'movementDetail.salePrice',
      sortable: true,
      textAlign: TextAlign.END,
    },
    {
      id: 'qty',
      header: 'movementDetail.qty',
      sortable: true,
      textAlign: TextAlign.END,
    },
    {
      id: 'inventoryQty',
      header: 'movementDetail.inventoryQty',
      sortable: true,
      textAlign: TextAlign.END,
    },
    {
      id: 'stockPosition.name',
      header: 'movementDetail.stockPosition',
      sortable: true,
      render: (movementDetail: IMovementDetail) => movementDetail.stockPosition?.name,
      sortField: 'stockPosition.name',
    },
  ],
  filters: {
    'product.code': {
      operator: FilterOperatorOptions.AND,
      constraints: [{ value: null, matchMode: FilterMatchMode.CONTAINS }],
    },
    'product.description': {
      operator: FilterOperatorOptions.AND,
      constraints: [{ value: null, matchMode: FilterMatchMode.CONTAINS }],
    },
    'product.uom.abbreviation': {
      operator: FilterOperatorOptions.AND,
      constraints: [{ value: null, matchMode: FilterMatchMode.CONTAINS }],
    },
    vendorCode: {
      operator: FilterOperatorOptions.AND,
      constraints: [{ value: null, matchMode: FilterMatchMode.CONTAINS }],
    },
    unitCost: { operator: FilterOperatorOptions.AND, constraints: [{ value: null, matchMode: FilterMatchMode.EQUALS }] },
    salePrice: { operator: FilterOperatorOptions.AND, constraints: [{ value: null, matchMode: FilterMatchMode.EQUALS }] },
    qty: { operator: FilterOperatorOptions.AND, constraints: [{ value: null, matchMode: FilterMatchMode.EQUALS }] },
    inventoryQty: {
      operator: FilterOperatorOptions.AND,
      constraints: [{ value: null, matchMode: FilterMatchMode.EQUALS }],
    },
    'stockPosition.name': {
      operator: FilterOperatorOptions.AND,
      constraints: [{ value: null, matchMode: FilterMatchMode.CONTAINS }],
    },
  },
  rowActions: [
    {
      ...buildRemoveAction(entityRouteName),
    },
  ],
}

export const movementDetailRouteName: string = entity.name
export const movementDetailEntity: IEntity = entity
export const movementDetailRoutes: IAction[] = entity.rowActions ? entity.rowActions : []
export default entity
