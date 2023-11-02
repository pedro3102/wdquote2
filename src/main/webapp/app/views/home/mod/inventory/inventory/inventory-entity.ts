import {IAction, IEntity} from '@/views/entities/common/entity-interface'
import appConstants from '@/shared/const/app-constants'
import {
  EditRowType,
  FilterDisplayType,
  FilterMatchMode,
  FilterOperatorOptions,
  FilterType,
  StockStatus,
  TextAlign,
} from '@/shared/const/entity-constants'
import {IInventory} from '@/views/home/mod/inventory/inventory/inventory-model'
import dayjs from 'dayjs'
import {DATE_FORMAT_USA} from '@/shared/composables/date-format'
import customRender from '@/shared/renders/custom-render'

const entityRouteName = 'inventories'

const entity: IEntity = {
  name: entityRouteName,
  classHumanized: 'inventory.detail.title',
  title: 'inventory.home.title',
  headerLabel: 'inventory.home.title',
  searchPlaceHolder: 'inventory.home.search',
  serverPaging: true,
  first: 0,
  pageSize: appConstants.APP.PAGE_SIZE,
  reverse: false,
  propOrder: 'id',
  exportCSV: true,
  filterDisplay: FilterDisplayType.MENU,
  dtEditRowInPlace: true,
  dtMaximizableModal: true,
  dtNoRowActions: true,
  fields: [
    {
      id: 'code',
      header: 'product.code',
      sortable: true,
      render: (inventory: IInventory) => inventory.product.code,
      filterType: FilterType.TEXT,
      sortField: 'product.code',
      textAlign: TextAlign.CENTER,
    },
    {
      id: 'description',
      header: 'product.description',
      render: (inventory: IInventory) => inventory.product.description,
      sortable: true,
      filterType: FilterType.TEXT,
      sortField: 'product.description',
    },
    {
      id: 'uom',
      header: 'product.uom',
      render: (inventory: IInventory) => inventory.product.uom.abbreviation,
      sortable: true,
      filterType: FilterType.TEXT,
      sortField: 'product.uom.abbreviation',
      textAlign: TextAlign.CENTER,
    },
    {
      id: 'unitCost',
      header: 'inventory.unitCost',
      sortable: true,
      filterType: FilterType.NUMERIC,
      textAlign: TextAlign.END,
    },
    {
      id: 'qty',
      header: 'inventory.qty',
      sortable: true,
      filterType: FilterType.NUMERIC,
      textAlign: TextAlign.END,
    },
    {
      id: 'reorderPoint',
      header: 'inventory.reorderPoint',
      sortable: true,
      filterType: FilterType.NUMERIC,
      textAlign: TextAlign.END,
      editRowType: EditRowType.NUMERIC,
    },
    {
      id: 'stockStatus',
      header: 'inventory.status',
      render: (inventory: IInventory) => customRender.renderStockStatus(inventory.stockStatus),
      sortField: 'reorderStatus',
      sortable: true,
      textAlign: TextAlign.CENTER,
      filterType: FilterType.SELECT,
      filterSelectOptions: () => Object.keys(StockStatus),
      renderFilterSelectOption: stockStatus => customRender.renderStockStatus(stockStatus.option),
    },
    {
      id: 'lastActivityDate',
      header: 'inventory.lastActivityDate',
      sortable: true,
      render: (inventory: IInventory) => dayjs(inventory.lastActivityDate).format(DATE_FORMAT_USA),
      filterType: FilterType.DATE,
      textAlign: TextAlign.CENTER,
    },
    {
      id: 'shelf',
      header: 'inventory.shelf',
      sortable: true,
      render: (inventory: IInventory) => inventory.shelf,
      filterType: FilterType.TEXT,
      editRowType: EditRowType.TEXT,
    },
    {
      id: 'vendorLeadTime',
      header: 'inventory.vendorLeadTime',
      sortable: true,
      filterType: FilterType.NUMERIC,
      textAlign: TextAlign.END,
      editRowType: EditRowType.NUMERIC,
    },
    {
      id: 'location',
      header: 'inventory.location',
      sortable: true,
      render: (inventory: IInventory) => inventory.location.code + '-' + inventory.location.name,
      filterType: FilterType.TEXT,
      sortField: 'location.code',
    },
  ],
  filters: {
    code: {operator: FilterOperatorOptions.AND, constraints: [{value: null, matchMode: FilterMatchMode.CONTAINS}]},
    description: {
      operator: FilterOperatorOptions.AND,
      constraints: [{value: null, matchMode: FilterMatchMode.CONTAINS}],
    },
    uom: {operator: FilterOperatorOptions.AND, constraints: [{value: null, matchMode: FilterMatchMode.EQUALS}]},
    unitCost: {operator: FilterOperatorOptions.AND, constraints: [{value: null, matchMode: FilterMatchMode.EQUALS}]},
    qty: {operator: FilterOperatorOptions.AND, constraints: [{value: null, matchMode: FilterMatchMode.EQUALS}]},
    shelf: {operator: FilterOperatorOptions.AND, constraints: [{value: null, matchMode: FilterMatchMode.EQUALS}]},
    reorderPoint: {
      operator: FilterOperatorOptions.AND,
      constraints: [{value: null, matchMode: FilterMatchMode.EQUALS}]
    },
    stockStatus: {operator: FilterOperatorOptions.AND, constraints: [{value: null, matchMode: FilterMatchMode.EQUALS}]},
    authorities: {operator: FilterOperatorOptions.AND, constraints: [{value: null, matchMode: FilterMatchMode.EQUALS}]},
    vendorLeadTime: {
      operator: FilterOperatorOptions.AND,
      constraints: [{value: null, matchMode: FilterMatchMode.CONTAINS}]
    },
    location: {operator: FilterOperatorOptions.AND, constraints: [{value: null, matchMode: FilterMatchMode.EQUALS}]},
  },
  leftToolsActions: [],
  rowActions: [],
}

export const inventoryRouteName: string = entity.name
export const inventoryEntity: IEntity = entity
export const inventoryRoutes: IAction[] = entity.rowActions ? entity.rowActions : []
export default entity
