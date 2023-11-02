import {IAction, IEntity} from '@/views/entities/common/entity-interface'
import customRender from '@/shared/renders/custom-render'
import appConstants from '@/shared/const/app-constants'
import {buildAddAction, buildRemoveAction, buildUpdateAction} from '@/views/entities/common/entity-api'
import {
  FilterDisplayType,
  FilterMatchMode,
  FilterOperatorOptions,
  FilterType,
  LocationType,
  TextAlign,
} from '@/shared/const/entity-constants'
import {ILocation} from '@/views/home/mod/inventory/location/location-model'

const entityRouteName = 'locations'

const entity: IEntity = {
  name: entityRouteName,
  classHumanized: 'location.detail.title',
  headerLabel: 'location.home.title',
  title: 'location.home.title',
  searchPlaceHolder: 'location.home.search',
  serverPaging: false,
  first: 0,
  pageSize: appConstants.APP.PAGE_SIZE,
  reverse: false,
  propOrder: 'id',
  exportCSV: true,
  globalFilterFields: ['code'],
  filterDisplay: FilterDisplayType.MENU,
  deleteMsgField: (item: any) => item.name,
  fields: [
    {
      id: 'code',
      header: 'location.code',
      sortable: true,
      filterType: FilterType.TEXT,
      textAlign: TextAlign.CENTER,
    },
    {
      id: 'name',
      header: 'location.name',
      sortable: true,
      filterType: FilterType.TEXT,
    },
    {
      id: 'address',
      header: 'location.address',
    },
    {
      id: 'locationType',
      header: 'location.locationType',
      sortable: true,
      filterType: FilterType.SELECT,
      render: (location: ILocation) => customRender.locationType(location.locationType),
      filterSelectOptions: () => Object.keys(LocationType),
      renderFilterSelectOption: data => customRender.locationType(data.option),
      textAlign: TextAlign.CENTER,
    },
    /*    {
      id: 'authorities',
      header: 'location.authorities',
      render: (location: IUser) => customRender.locationType(location.authorities),
    },*/
  ],
  filters: {
    code: {operator: FilterOperatorOptions.AND, constraints: [{value: null, matchMode: FilterMatchMode.CONTAINS}]},
    name: {operator: FilterOperatorOptions.AND, constraints: [{value: null, matchMode: FilterMatchMode.CONTAINS}]},
    locationType: {
      operator: FilterOperatorOptions.AND,
      constraints: [{value: null, matchMode: FilterMatchMode.EQUALS}]
    },
  },
  leftToolsActions: [
    /*    {
      id: 'example',
      path: 'load-example',
      name: buildCreateRouteName(exampleRouteName),
      type: ActionType.LINK,
      label: () => 'label',
      title: () => 'title',
      class: () => 'css class',
      icon: () => 'font awesome icon',
      iconColor: () => 'color para icon',
      visible: (): boolean => manage is visible,
      execute: () => buildCreateRoute(exampleRouteName)
    },*/
    buildAddAction(entityRouteName, 'location.home.createLabel'),
  ],
  rowActions: [
    {
      ...buildUpdateAction(entityRouteName),
    },
    {
      ...buildRemoveAction(entityRouteName),
    },
  ],
}

export const locationRouteName: string = entity.name
export const locationEntity: IEntity = entity
export const locationRoutes: IAction[] = entity.rowActions ? entity.rowActions : []
export default entity
