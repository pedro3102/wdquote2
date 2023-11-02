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
import { IUnitOfMeasure } from '@/views/home/mod/inventory/unit-of-measure/unit-of-measure-model'
import customRender from '@/shared/renders/custom-render'
import {
  buildAddConversionAction,
  unitOfMeasureConversionEntity,
  unitOfMeasureConversionRouteName,
} from '@/views/home/mod/inventory/unit-of-measure-conversion/unit-of-measure-conversion-entity'

const entityRouteName = 'unit-of-measures'

const entity: IEntity = {
  name: entityRouteName,
  classHumanized: 'unitOfMeasure.detail.title',
  title: 'unitOfMeasure.home.title',
  headerLabel: 'unitOfMeasure.home.title',
  searchPlaceHolder: 'unitOfMeasure.home.search',
  serverPaging: false,
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
      id: 'name',
      header: 'unitOfMeasure.name',
      sortable: true,
      filterType: FilterType.TEXT,
    },
    {
      id: 'abbreviation',
      header: 'unitOfMeasure.abbreviation',
      sortable: true,
      filterType: FilterType.TEXT,
    },
    {
      id: 'allowsDecimal',
      header: 'unitOfMeasure.allowsDecimal',
      renderType: RenderType.BOOLEAN,
      textAlign: TextAlign.CENTER,
      render: (unitOfMeasure: IUnitOfMeasure) => customRender.boolean(unitOfMeasure.allowsDecimal),
      sortable: true,
      dataType: DataType.BOOLEAN,
      filterType: FilterType.BOOLEAN,
    },
  ],
  filters: {
    name: { operator: FilterOperatorOptions.AND, constraints: [{ value: null, matchMode: FilterMatchMode.CONTAINS }] },
    abbreviation: {
      operator: FilterOperatorOptions.AND,
      constraints: [{ value: null, matchMode: FilterMatchMode.CONTAINS }],
    },
    allowsDecimal: { value: null, matchMode: FilterMatchMode.EQUALS },
  },
  leftToolsActions: [buildAddAction(entityRouteName, 'unitOfMeasure.home.createLabel')],
  rowActions: [
    {
      ...buildUpdateAction(entityRouteName),
    },
    {
      ...buildAddConversionAction(unitOfMeasureConversionRouteName),
    },
    {
      ...buildRemoveAction(entityRouteName),
    },
  ],
  children: unitOfMeasureConversionEntity,
}

export const unitOfMeasureRouteName: string = entity.name
export const unitOfMeasureEntity: IEntity = entity
export const unitOfMeasureRoutes: IAction[] = entity.rowActions ? entity.rowActions : []
export default entity
