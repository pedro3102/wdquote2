import { IAction, IEntity } from '@/views/entities/common/entity-interface'
import appConstants from '@/shared/const/app-constants'
import { buildAddAction, buildRemoveAction, buildUpdateAction } from '@/views/entities/common/entity-api'
import { FilterDisplayType, FilterMatchMode, FilterOperatorOptions, FilterType, TextAlign } from '@/shared/const/entity-constants'
import { IProduct } from '@/views/home/mod/inventory/product/product-model'

const entityRouteName = 'products'

const renderSystemModels = data => {
  if (data) {
    return data.map(a => a.name).join(', ')
  }
  return ''
}

const entity: IEntity = {
  name: entityRouteName,
  classHumanized: 'product.detail.title',
  headerLabel: 'product.home.title',
  title: 'product.home.title',
  searchPlaceHolder: 'product.home.search',
  serverPaging: true,
  first: 0,
  pageSize: appConstants.APP.PAGE_SIZE,
  reverse: false,
  propOrder: 'id',
  exportCSV: true,
  dtMaximizableModal: true,
  filterDisplay: FilterDisplayType.MENU,
  deleteMsgField: (item: any) => item.code,
  fields: [
    {
      id: 'code',
      header: 'product.code',
      sortable: true,
      filterType: FilterType.TEXT,
    },
    {
      id: 'description',
      header: 'product.description',
      sortable: true,
      filterType: FilterType.TEXT,
    },
    {
      id: 'category',
      header: 'product.category',
      sortable: true,
      render: (product: IProduct) => product.category.name,
      filterType: FilterType.TEXT,
    },
    {
      id: 'uom',
      header: 'product.uom',
      sortable: true,
      render: (product: IProduct) => product.uom.abbreviation,
      filterType: FilterType.TEXT,
    },
    {
      id: 'weight',
      header: 'product.weight',
      sortable: true,
      filterType: FilterType.NUMERIC,
      textAlign: TextAlign.END,
    },
    {
      id: 'uomWeight',
      header: 'product.uomWeight',
      sortable: true,
      render: (product: IProduct) => product.uomWeight?.abbreviation,
      filterType: FilterType.TEXT,
    },
    {
      id: 'systemModels',
      header: 'product.systemModel',
      sortable: false,
      render: (product: IProduct) => renderSystemModels(product.systemModels),
      filterType: FilterType.TEXT,
    },
  ],
  filters: {
    code: { operator: FilterOperatorOptions.AND, constraints: [{ value: null, matchMode: FilterMatchMode.CONTAINS }] },
    description: {
      operator: FilterOperatorOptions.AND,
      constraints: [{ value: null, matchMode: FilterMatchMode.CONTAINS }],
    },
    category: { operator: FilterOperatorOptions.AND, constraints: [{ value: null, matchMode: FilterMatchMode.EQUALS }] },
    uom: { operator: FilterOperatorOptions.AND, constraints: [{ value: null, matchMode: FilterMatchMode.EQUALS }] },
    uomWeight: { operator: FilterOperatorOptions.AND, constraints: [{ value: null, matchMode: FilterMatchMode.EQUALS }] },
    weight: { operator: FilterOperatorOptions.AND, constraints: [{ value: null, matchMode: FilterMatchMode.EQUALS }] },
    systemModels: { operator: FilterOperatorOptions.AND, constraints: [{ value: null, matchMode: FilterMatchMode.CONTAINS }] },
  },
  leftToolsActions: [buildAddAction(entityRouteName, 'product.home.createLabel')],
  rowActions: [
    {
      ...buildUpdateAction(entityRouteName),
    },
    {
      ...buildRemoveAction(entityRouteName),
    },
  ],
}

export const productRouteName: string = entity.name
export const productEntity: IEntity = entity
export const productRoutes: IAction[] = entity.rowActions ? entity.rowActions : []
export default entity
