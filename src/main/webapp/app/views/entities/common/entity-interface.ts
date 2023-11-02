import {
  ActionTarget,
  ActionType,
  FilterDisplayType,
  FilterMatchMode,
  FilterOperatorOptions,
  DataType,
  RenderType,
  SortModeType,
  DatatableSize,
  FilterType,
  TextAlign,
  EditRowType,
  ScrollHeight,
} from '@/shared/const/entity-constants'

export interface ICurrentSelection {
  index: number
  value: object
  status: boolean
}

export interface ISelection {
  selected: object | null
  current: ICurrentSelection | null
}

export interface IBaseFilter {
  value?: string | number
  matchMode?: FilterMatchMode
  type?: DataType
  operator?: FilterOperatorOptions
  constraints?: IBaseFilter[]
}

export interface IFilter {
  [name: string]: IBaseFilter
}

export interface IPrimeRenderData {
  data: string
  severity?: string
}

export interface IScrollConf {
  scrollable: boolean
  scrollHeight?: ScrollHeight
}

export interface IField {
  id: string
  header: string
  dtAddRowInPlaceDefault?: any
  textAlign?: TextAlign
  title?: string
  iconClass?: string
  renderClass?: string
  renderTooltipMessage?: string
  sortable?: boolean
  renderType?: RenderType
  render?: (row: object, field?: IField) => string
  renderFilterSelectOption?: (row: object, field?: IField) => string
  filterSelectOptions?: (row?: object, field?: IField) => any[]
  changeStatusServName?: string
  changeStatusAlertLabel?: string
  dataType?: DataType
  filterType?: FilterType
  editRowType?: EditRowType
  selectOptionLabel?: string
  selectOptionValue?: string
  sortField?: string
}

export interface IAction {
  id: string
  path: string
  name: string
  title?: (row?: object) => string
  label?: (row?: object) => string
  breadcrumbTitle?: () => string
  sortable?: () => boolean
  type?: ActionType
  target?: ActionTarget
  class?: (row?: object) => string
  icon?: (row?: object) => string
  iconColor?: (row?: object) => string
  visible?: (row?: any) => boolean
  visibleOnContextMenu?: (row?: object) => boolean
  execute?: (field?: object, emit?: any) => any
  childActions?: IAction[] // Only available when type ActionType.DROPDOWN
}

export interface IEntity {
  name: string
  classHumanized: string
  headerLabel: string
  title: string
  searchPlaceHolder: string
  // Dt Pagination
  serverPaging: boolean
  dtDisablePaginator?: boolean
  first?: number
  pageSize?: number
  reverse?: boolean
  propOrder?: string
  // Filter
  filterDisplay?: FilterDisplayType
  findParamProp?: string
  disableGlobalFilter?: boolean
  globalFilterFields?: string[]
  //actions
  disableRowActions?: boolean
  disableHeaderActions?: boolean
  dtDisableGridlines?: boolean
  dtEditRowInPlace?: boolean
  dtAddRowInPlace?: boolean
  dtScrollConf?: IScrollConf
  dtMaximizableModal?: boolean
  dtNoRowActions?: boolean
  fields: IField[]
  filters?: IFilter
  rowActions?: IAction[]
  keyBackProperty?: string
  leftToolsActions?: IAction[]
  rightToolsActions?: IAction[]
  bottomActions?: IAction[]
  deleteMsgField?: (row: object) => string
  onBeforeRowLoad?: (row: object) => object
  watchOnRouteName?: string // Apply filter on route name to watch
  exportCSV?: boolean
  sortMode?: SortModeType
  datatableSize?: DatatableSize
  children?: IEntity
  childrenHeaderLabelRender?: (row: object) => string
}

export interface ICardRow {
  title?: (row?: object) => string
  iconClass?: (row?: object) => string
  hideMask?: boolean
  link?: boolean
  linkRender?: (row: object) => string
  valueRender: (row: object) => string | number
}

export interface IDropDown {
  title?: (row: object, other: object) => string
  text?: (row: object) => string
  class?: (row: object) => string
  iconClass?: (row: object) => string
  itemsActionText?: (row: object) => string
  itemsActionTitle?: (row: object, other: object) => string
  itemsActionIconClass?: (row: object, other: object) => string
  itemsActionExecute?: (field: object) => any
  itemsActions?: IAction[]
}

export interface ICard<T> {
  title?: (row?: T) => string
  onLoadCurrent?: (row?: T) => Promise<T>
  onLoadRelationship?: (row?: T) => Promise<T[]>
  dropDownButton?: IDropDown
  items?: ICardRow[]
}

export interface IEntityDetails<T> {
  name: string
  parentAction: IAction
  imageTitle: (row: T) => string
  imageSrc: (row: T) => string
  imageForceUpload: (row: T) => boolean
  onImageError: (event: Event, row: T) => void
  onImageChange?: (resultImage: object, item: T) => Promise<void>
  onBeforeDataLoad?: (row: T) => object
  onLoad?: (id: number) => Promise<T>
  mainCard?: ICard<T>
  secondaryCard?: ICard<T>
  tabActions: IAction[]
}

export interface IDTExpanded {
  defaultExpanded?: boolean
}
