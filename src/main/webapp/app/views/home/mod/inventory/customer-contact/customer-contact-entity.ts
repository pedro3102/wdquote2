import { IAction, IEntity } from '@/views/entities/common/entity-interface'
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
import { ICustomerContact } from '@/views/home/mod/inventory/customer-contact/customer-contact-model'
import customRender from '@/shared/renders/custom-render'
import TranslationService from '@/locale/translation-service'
import { ICustomer } from '@/views/home/mod/inventory/customer/customer-model'

const entityRouteName = 'customer-contacts'

const filters = {
  code: { operator: FilterOperatorOptions.AND, constraints: [{ value: null, matchMode: FilterMatchMode.CONTAINS }] },
  name: { operator: FilterOperatorOptions.AND, constraints: [{ value: null, matchMode: FilterMatchMode.CONTAINS }] },
  phone: { operator: FilterOperatorOptions.AND, constraints: [{ value: null, matchMode: FilterMatchMode.CONTAINS }] },
  email: { operator: FilterOperatorOptions.AND, constraints: [{ value: null, matchMode: FilterMatchMode.CONTAINS }] },
  salespersonCode: {
    operator: FilterOperatorOptions.AND,
    constraints: [{ value: null, matchMode: FilterMatchMode.CONTAINS }],
  },
  isDefaultContact: { value: null, matchMode: FilterMatchMode.EQUALS },
  language: { operator: FilterOperatorOptions.AND, constraints: [{ value: null, matchMode: FilterMatchMode.CONTAINS }] },
}

const fields = [
  {
    id: 'code',
    header: 'customerContact.code',
    sortable: true,
    filterType: FilterType.TEXT,
  },
  {
    id: 'name',
    header: 'customerContact.name',
    sortable: true,
    filterType: FilterType.TEXT,
  },
  {
    id: 'phone',
    header: 'customerContact.phone',
    sortable: true,
    filterType: FilterType.TEXT,
  },
  {
    id: 'email',
    header: 'customerContact.email',
    sortable: true,
    filterType: FilterType.TEXT,
  },
  {
    id: 'salespersonCode',
    header: 'customerContact.salespersonCode',
    sortable: true,
    filterType: FilterType.TEXT,
  },
  {
    id: 'language',
    header: 'customerContact.language',
    sortable: true,
    sortField: 'language.code',
    render: (customerContact: ICustomerContact) => customerContact.language.code + '-' + customerContact.language.name,
    filterType: FilterType.TEXT,
  },
  {
    id: 'isDefaultContact',
    header: 'customerContact.isDefaultContact',
    renderType: RenderType.BOOLEAN,
    render: (customerContact: ICustomerContact) => customRender.boolean(customerContact.isDefaultContact),
    sortable: true,
    dataType: DataType.BOOLEAN,
    filterType: FilterType.BOOLEAN,
    textAlign: TextAlign.CENTER,
  },
]

const entity: IEntity = {
  name: entityRouteName,
  classHumanized: 'customerContact.detail.title',
  title: 'customerContact.home.title',
  headerLabel: 'customerContact.home.title',
  searchPlaceHolder: 'customerContact.home.search',
  serverPaging: true,
  first: 0,
  propOrder: 'id',
  filterDisplay: FilterDisplayType.MENU,
  deleteMsgField: (item: any) => item.name,
  dtMaximizableModal: true,
  fields: [
    {
      id: 'customer',
      header: 'customerContact.customer',
      sortable: true,
      render: (customerContact: ICustomerContact) => customerContact.customer.code + '-' + customerContact.customer.name,
      filterType: FilterType.TEXT,
      sortField: 'customer.code',
    },
    ...fields,
  ],
  filters: {
    customer: { operator: FilterOperatorOptions.AND, constraints: [{ value: null, matchMode: FilterMatchMode.CONTAINS }] },
    ...filters,
  },
  leftToolsActions: [buildAddAction(entityRouteName, 'customerContact.home.createLabel')],
  rowActions: [
    {
      ...buildUpdateAction(entityRouteName),
    },
    {
      ...buildRemoveAction(entityRouteName),
    },
  ],
}

const entityChild: IEntity = {
  ...entity,
  dtDisablePaginator: true,
  disableGlobalFilter: true,
  dtAddRowInPlace: true,
  dtEditRowInPlace: true,
  dtMaximizableModal: true,
  childrenHeaderLabelRender: (row: ICustomer) => TranslationService.getInstanced().t('customer.detail.childTitle', { customer: row.name }),

  fields: [...fields],
  filters: {
    ...filters,
  },
  leftToolsActions: [],
  rowActions: [
    {
      ...buildRemoveAction(entityRouteName),
    },
  ],
}

export const customerContactRouteName: string = entity.name
export const customerContactEntity: IEntity = entity
export const customerChildContactEntity: IEntity = entityChild
export const customerContactRoutes: IAction[] = entity.rowActions ? entity.rowActions : []
export default entity
