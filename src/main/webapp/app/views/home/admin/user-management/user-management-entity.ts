import { IAction, IEntity } from '@/views/entities/common/entity-interface'
import customRender from '@/shared/renders/custom-render'
import appConstants from '@/shared/const/app-constants'
import { buildAddAction, buildRemoveAction, buildUpdateAction } from '@/views/entities/common/entity-api'
import { IUser } from '@/views/home/admin/user-management/user.model'
import {
  FilterDisplayType,
  FilterMatchMode,
  FilterOperatorOptions,
  DataType,
  RenderType,
  FilterType,
  PrimeSeverities,
  EditRowType,
} from '@/shared/const/entity-constants'
import { ROLES } from '@/shared/const/auth-constants'

const entityRouteName = 'users'

const entity: IEntity = {
  name: entityRouteName,
  classHumanized: 'user.detail.title',
  headerLabel: 'user.home.title',
  title: 'user.home.title',
  searchPlaceHolder: 'user.home.search',
  serverPaging: true,
  first: 0,
  pageSize: appConstants.APP.PAGE_SIZE,
  reverse: false,
  propOrder: 'id',
  findParamProp: 'login',
  exportCSV: true,
  globalFilterFields: ['login'],
  keyBackProperty: 'login',
  filterDisplay: FilterDisplayType.MENU,
  dtEditRowInPlace: true,
  dtAddRowInPlace: true,
  dtMaximizableModal: true,
  deleteMsgField: (item: any) => item.login,
  fields: [
    {
      id: 'id',
      header: 'user.id',
      dtAddRowInPlaceDefault: null,
    },
    {
      id: 'login',
      header: 'user.login',
      sortable: true,
      filterType: FilterType.TEXT,
      editRowType: EditRowType.TEXT,
    },
    {
      id: 'email',
      header: 'user.email',
      sortable: true,
      filterType: FilterType.TEXT,
      editRowType: EditRowType.TEXT,
    },
    {
      id: 'firstName',
      header: 'user.firstName',
      sortable: true,
      editRowType: EditRowType.TEXT,
    },
    {
      id: 'lastName',
      header: 'user.lastName',
      sortable: true,
      editRowType: EditRowType.TEXT,
    },
    {
      id: 'activated',
      header: 'user.activated',
      renderType: RenderType.BOOLEAN,
      render: (user: IUser) => customRender.boolean(user.activated),
      renderClass: 'cursor-pointer',
      renderTooltipMessage: 'entity.action.changeStatus.header',
      changeStatusServName: 'changeStatus',
      changeStatusAlertLabel: 'login',
      sortable: true,
      dataType: DataType.BOOLEAN,
      filterType: FilterType.BOOLEAN,
    },
    {
      id: 'authorities',
      header: 'user.authorities',
      filterType: FilterType.SELECT,
      editRowType: EditRowType.SELECT,
      render: (user: IUser) => customRender.userAuthorities(user.authorities),
      filterSelectOptions: () => Object.keys(ROLES),
      renderFilterSelectOption: data =>
        customRender.renderPrimeTag(data.option, data.option === ROLES.ROLE_ADMIN ? PrimeSeverities.DANGER : PrimeSeverities.INFO),
    },
  ],
  filters: {
    login: { operator: FilterOperatorOptions.AND, constraints: [{ value: null, matchMode: FilterMatchMode.CONTAINS }] },
    email: { operator: FilterOperatorOptions.AND, constraints: [{ value: null, matchMode: FilterMatchMode.CONTAINS }] },
    activated: { value: null, matchMode: FilterMatchMode.EQUALS },
    authorities: { operator: FilterOperatorOptions.AND, constraints: [{ value: null, matchMode: FilterMatchMode.EQUALS }] },
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
      iconClass: () => 'font awesome icon',
      visible: (): boolean => manage is visible,
      execute: () => buildCreateRoute(exampleRouteName)
    },*/
    /*buildAddAction(entityRouteName, 'user.home.createLabel'),*/
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

export const userRouteName: string = entity.name
export const userManageEntity: IEntity = entity
export const userRoutes: IAction[] = entity.rowActions ? entity.rowActions : []
export default entity
