import { IAction, IEntity } from '@/views/entities/common/entity-interface'
import appConstants from '@/shared/const/app-constants'
import { buildAddAction, buildRemoveAction, buildUpdateAction } from '@/views/entities/common/entity-api'
import {
  DataType,
  FilterDisplayType,
  FilterMatchMode,
  FilterOperatorOptions,
  FilterType,
  RenderType,
  TextAlign,
} from '@/shared/const/entity-constants'
import { ICustomer } from '@/views/home/mod/inventory/customer/customer-model'
import customRender from '@/shared/renders/custom-render'
import { customerChildContactEntity } from '@/views/home/mod/inventory/customer-contact/customer-contact-entity'

const entityRouteName = 'customers'

const entity: IEntity = {
  name: entityRouteName,
  classHumanized: 'customer.detail.title',
  headerLabel: 'customer.home.title',
  title: 'customer.home.title',
  searchPlaceHolder: 'customer.home.search',
  serverPaging: false,
  first: 0,
  pageSize: appConstants.APP.PAGE_MAX_SAFE_SIZE,
  propOrder: 'id',
  exportCSV: true,
  filterDisplay: FilterDisplayType.MENU,
  deleteMsgField: (item: any) => item.code,
  dtMaximizableModal: true,
  children: customerChildContactEntity,

  fields: [
    {
      id: 'code',
      header: 'customer.code',
      sortable: true,
      filterType: FilterType.TEXT,
    },
    {
      id: 'name',
      header: 'customer.name',
      sortable: true,
      filterType: FilterType.TEXT,
    },
    {
      id: 'address',
      header: 'customer.address',
      sortable: true,
      filterType: FilterType.TEXT,
    },
    {
      id: 'taxLiable',
      header: 'customer.taxLiable',
      renderType: RenderType.BOOLEAN,
      render: (customer: ICustomer) => customRender.boolean(customer.taxLiable),
      sortable: true,
      dataType: DataType.BOOLEAN,
      filterType: FilterType.BOOLEAN,
      textAlign: TextAlign.CENTER,
    },
    {
      id: 'taxExemptionCode',
      header: 'customer.taxExemptionCode',
      sortable: true,
      filterType: FilterType.TEXT,
    },
    {
      id: 'paymentTerms',
      header: 'customer.paymentTerms',
      sortable: true,
      filterType: FilterType.TEXT,
    },
    {
      id: 'creditLimit',
      header: 'customer.creditLimit',
      sortable: true,
      filterType: FilterType.NUMERIC,
      textAlign: TextAlign.END,
    },
    {
      id: 'deliveryZone.name',
      header: 'customer.deliveryZone',
      sortable: true,
      render: (customer: ICustomer) => customer.deliveryZone.name,
      filterType: FilterType.TEXT,
    },
    {
      id: 'taxAreaCode.name',
      header: 'customer.taxAreaCode',
      sortable: true,
      render: (customer: ICustomer) => customer.taxAreaCode.name,
      filterType: FilterType.TEXT,
    },
    {
      id: 'blocked',
      header: 'customer.blocked',
      renderType: RenderType.BOOLEAN,
      render: (customer: ICustomer) => customRender.boolean(customer.blocked),
      sortable: true,
      dataType: DataType.BOOLEAN,
      filterType: FilterType.BOOLEAN,
      textAlign: TextAlign.CENTER,
    },
  ],
  filters: {
    code: { operator: FilterOperatorOptions.AND, constraints: [{ value: null, matchMode: FilterMatchMode.CONTAINS }] },
    name: { operator: FilterOperatorOptions.AND, constraints: [{ value: null, matchMode: FilterMatchMode.CONTAINS }] },
    address: { operator: FilterOperatorOptions.AND, constraints: [{ value: null, matchMode: FilterMatchMode.CONTAINS }] },
    taxLiable: { value: null, matchMode: FilterMatchMode.EQUALS },
    taxExemptionCode: {
      operator: FilterOperatorOptions.AND,
      constraints: [{ value: null, matchMode: FilterMatchMode.CONTAINS }],
    },
    paymentTerms: {
      operator: FilterOperatorOptions.AND,
      constraints: [{ value: null, matchMode: FilterMatchMode.CONTAINS }],
    },
    creditLimit: {
      operator: FilterOperatorOptions.AND,
      constraints: [{ value: null, matchMode: FilterMatchMode.CONTAINS }],
    },
    'deliveryZone.name': {
      operator: FilterOperatorOptions.AND,
      constraints: [{ value: null, matchMode: FilterMatchMode.CONTAINS }],
    },
    'taxAreaCode.name': {
      operator: FilterOperatorOptions.AND,
      constraints: [{ value: null, matchMode: FilterMatchMode.CONTAINS }],
    },
    blocked: { value: null, matchMode: FilterMatchMode.EQUALS },
  },
  leftToolsActions: [buildAddAction(entityRouteName, 'customer.home.createLabel')],
  rowActions: [
    {
      ...buildUpdateAction(entityRouteName),
    },
    {
      ...buildRemoveAction(entityRouteName),
    },
  ],
}

export const customerRouteName: string = entity.name
export const customerEntity: IEntity = entity
export const customerRoutes: IAction[] = entity.rowActions ? entity.rowActions : []
export default entity
