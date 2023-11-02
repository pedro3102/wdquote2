import { IAction, IEntity } from '@/views/entities/common/entity-interface'
import appConstants from '@/shared/const/app-constants'
import { buildAddAction, buildRemoveAction, buildUpdateAction } from '@/views/entities/common/entity-api'
import {
  ActionType,
  FilterDisplayType,
  FilterMatchMode,
  FilterOperatorOptions,
  FilterType,
  SeverityCodes,
  TextAlign,
} from '@/shared/const/entity-constants'
import { IUnitOfMeasureConversion } from '@/views/home/mod/inventory/unit-of-measure-conversion/unit-of-measure-conversion-model'
import { RouteLocationRaw } from 'vue-router'
import { buildCreateRouteName, buildUpdateRouteName } from '@/shared/config/router/router-utils'
import { IUnitOfMeasure } from '@/views/home/mod/inventory/unit-of-measure/unit-of-measure-model'
import TranslationService from '@/locale/translation-service'

const entityRouteName = 'unit-of-measure-conversions'

export const buildAddConversionRoute = (routeName: string, item: object, property?: string, params?: object): RouteLocationRaw => {
  const prop = property || 'id'
  return {
    name: buildCreateRouteName(routeName),
    params: { [prop]: item ? item[prop] : null, ...params },
  }
}

const buildUpdateConversionRoute = (routeName: string, item: object, property?: string, params?: object): RouteLocationRaw => {
  return {
    name: buildUpdateRouteName(routeName),
    params: { ...params },
  }
}

export const buildAddConversionAction = (entityRouteName: string): IAction => ({
  id: 'conversion',
  path: 'conversion-create',
  name: buildCreateRouteName(entityRouteName),
  type: ActionType.LINK,
  label: () => 'unitOfMeasure.home.createConversionLabel',
  title: () => 'unitOfMeasure.home.createConversionLabel',
  class: () => '',
  icon: () => 'retweet',
  iconColor: () => SeverityCodes.INFO,
  visible: (): boolean => true,
  execute: (row: object, property?: string) => buildAddConversionRoute(entityRouteName, row, property),
})

const buildUpdateConversionAction = (entityRouteName: string): IAction => ({
  id: 'conversion',
  path: 'conversion-edit',
  name: buildUpdateRouteName(entityRouteName),
  type: ActionType.LINK,
  label: () => 'unitOfMeasure.home.updateConversionLabel',
  title: () => 'unitOfMeasure.home.updateConversionLabel',
  class: () => '',
  icon: () => 'pencil',
  iconColor: () => SeverityCodes.PRIMARY,
  visible: (): boolean => true,
  execute: (row: object, property?: string) =>
    buildUpdateConversionRoute(entityRouteName, row, property, { id: row.uom.id, convId: row.id }),
})

const entity: IEntity = {
  name: entityRouteName,
  classHumanized: 'unitOfMeasureConversion.detail.title',
  title: 'unitOfMeasureConversion.home.title',
  headerLabel: 'unitOfMeasureConversion.home.title',
  searchPlaceHolder: 'unitOfMeasureConversion.home.search',
  serverPaging: false,
  first: 0,
  pageSize: appConstants.APP.PAGE_MAX_SAFE_SIZE,
  propOrder: 'id',
  filterDisplay: FilterDisplayType.MENU,
  deleteMsgField: (item: any) => item.name,
  dtDisablePaginator: true,
  disableGlobalFilter: true,
  dtAddRowInPlace: true,
  dtEditRowInPlace: true,
  dtMaximizableModal: true,
  childrenHeaderLabelRender: (row: IUnitOfMeasure) =>
    TranslationService.getInstanced().t('unitOfMeasureConversion.detail.childTitle', { uom: row.name }),
  fields: [
    {
      id: 'uom',
      header: 'unitOfMeasureConversion.uom',
      sortable: true,
      render: (unitOfMeasureConversion: IUnitOfMeasureConversion) => unitOfMeasureConversion?.uom?.name || '',
      filterType: FilterType.TEXT,
    },
    {
      id: 'conversionFactor',
      header: 'unitOfMeasureConversion.conversionFactor',
      sortable: true,
      textAlign: TextAlign.END,
    },
    {
      id: 'uomEquivalent',
      header: 'unitOfMeasureConversion.uomEquivalent',
      sortable: true,
      render: (unitOfMeasureConversion: IUnitOfMeasureConversion) => unitOfMeasureConversion?.uomEquivalent?.name || '',
      filterType: FilterType.TEXT,
    },
  ],
  filters: {
    uom: { operator: FilterOperatorOptions.AND, constraints: [{ value: null, matchMode: FilterMatchMode.EQUALS }] },
    uomEquivalent: { operator: FilterOperatorOptions.AND, constraints: [{ value: null, matchMode: FilterMatchMode.EQUALS }] },
  },
  rowActions: [
    {
      ...buildUpdateConversionAction(entityRouteName),
    },
    {
      ...buildRemoveAction(entityRouteName),
    },
  ],
}

export const unitOfMeasureConversionRouteName: string = entity.name
export const unitOfMeasureConversionEntity: IEntity = entity
export const unitOfMeasureConversionRoutes: IAction[] = entity.rowActions ? entity.rowActions : []
export default entity
