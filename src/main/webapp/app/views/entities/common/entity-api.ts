import { computed, reactive, ref } from 'vue'
import { getPageSize } from '@/shared/api/http/request'
import { IAction, IBaseFilter, IEntity, IField, IFilter } from '@/views/entities/common/entity-interface'
import { ActionType, BasicAction, FilterMatchMode, DataType, SeverityCodes } from '@/shared/const/entity-constants'
import {
  buildCreateRoute,
  buildCreateRouteName,
  buildDetailsRoute,
  buildDetailsRouteName,
  buildRemoveRouteName,
  buildUpdateRoute,
  buildUpdateRouteName,
} from '@/shared/config/router/router-utils'
import { localStorageInstance } from '@/shared/services/storage/local-storage-service'
import { mergeDeep, objectIsEmpty } from '@/shared/util'

/**
 * Build the Entity filter storage key.
 *
 * @param entityName The entity name.
 */
export const buildEntityFilterStorageKey = (entityName: string): string => `${entityName}-filter`

/**
 * Build the default Global Filter
 */
export const buildGlobalPrimeDTFilter = (): IFilter => ({
  global: { value: null, matchMode: FilterMatchMode.CONTAINS, type: DataType.TEXT },
})

/**
 * Init the prime filter when an initial filter is passed.
 *
 * @param entityFilters The entity filters.
 * @param initialFilter The initial filters values.
 */
export const initFilter = (entityFilters: IFilter = {}, initialFilter: IFilter = {}): IFilter =>
  mergeDeep(buildGlobalPrimeDTFilter(), mergeDeep(entityFilters, initialFilter))

/**
 * Convert to Base Filter from Entity Filters.
 *
 * @param entityFilters The Entity Filters.
 */
/*export const toBaseFilter = (entityFilters: IFilter[] = []): IBaseFilter[] => {
  const filters: IBaseFilter[] = []
  if (entityFilters) {
    entityFilters.forEach((filter: IFilter) => {
      const { name, type, operator, value } = filter as IBaseFilter
      filters.push({
        name,
        type,
        operator,
        value,
      })
    })
    return filters
  }
  return filters
}*/

const parseMatchMode = (mode: string): string => {
  switch (mode) {
    case FilterMatchMode.NOT_CONTAINS:
      return 'doesNotContain'
    case FilterMatchMode.LESS_THAN_OR_EQUAL_TO:
      return 'lessThanOrEqual'
    case FilterMatchMode.GREATER_THAN_OR_EQUAL_TO:
      return 'greaterThanOrEqual'
    default:
      return mode
  }
}

/**
 * Translate the Entity Filters to request query object.
 *
 * @param entityFilters The Entity Filters.
 */
export const toCriteriaFilterObject = (entityFilters: IFilter): object => {
  const filterObject = {}
  if (!objectIsEmpty({ ...entityFilters })) {
    return Object.entries(entityFilters).reduce((acc: object, curr: IBaseFilter) => {
      if (curr[1].value !== undefined && curr[1].value !== null) {
        return {
          [`${curr[0]}.${parseMatchMode(curr[1].matchMode)}`]: curr[1].value,
          ...acc,
        }
      } else if (curr[1].constraints?.length > 0) {
        const result = {}
        curr[1].constraints.forEach(rule => {
          if (rule.value) return Object.assign(result, { [`${curr[0]}.${parseMatchMode(rule.matchMode)}`]: rule.value })
        })
        return {
          ...result,
          ...acc,
        }
      }
      return acc
    }, filterObject)
  }
  return filterObject
}

/**
 * Load stored filters in Local Storage.
 *
 * @param entityName The Entityname.
 */
export const loadFilterFromStorage = (entityName: string): IBaseFilter[] =>
  localStorageInstance.getItem(buildEntityFilterStorageKey(entityName), [])

/**
 * Save the Entity Filters to Local Storage.
 *
 * @param entityName The Entity name.
 * @param filters The entity Filters.
 */
export const saveFilterFromStorage = (entityName: string, filters: IBaseFilter[]): void =>
  localStorageInstance.setItem(buildEntityFilterStorageKey(entityName), filters)

/**
 * Build the default Separator action
 *
 */
export const buildSeparatorAction = (isVisible?: (row) => boolean): IAction => ({
  id: 'separator',
  path: '',
  name: '',
  type: ActionType.DIVIDER,
  class: (): string => 'dropdown-divider',
  visible: (row: object): boolean => {
    if (isVisible) {
      return isVisible(row)
    }
    return true
  },
})

/**
 * Build the default Add action
 *
 * @param entityName The entity route name.
 * @param entityTitle The entity title/label.
 */
export const buildAddAction = (entityName: string, entityTitle: string): IAction => ({
  id: 'add',
  path: 'add',
  name: buildCreateRouteName(entityName),
  type: ActionType.LINK,
  label: () => entityTitle,
  title: () => entityTitle,
  class: () => '',
  icon: () => 'plus',
  iconColor: () => SeverityCodes.SUCCESS,
  visible: (): boolean => true,
  execute: () => buildCreateRoute(entityName),
})

/**
 * Build the default Edit/Update action
 *
 * @param entityRouteName The entity route name.
 */
export const buildUpdateAction = (entityRouteName: string): IAction => ({
  id: 'edit',
  path: ':id/edit',
  name: buildUpdateRouteName(entityRouteName),
  type: ActionType.LINK,
  label: () => 'entity.action.edit',
  title: () => 'entity.action.edit',
  class: () => '',
  icon: () => 'pencil',
  iconColor: () => SeverityCodes.PRIMARY,
  visible: (): boolean => true,
  execute: (row: object, property?: string) => buildUpdateRoute(entityRouteName, row, property),
})

/**
 * Build the default Edit/Update action
 *
 * @param entityRouteName The entity route name.
 */
export const buildDetailsAction = (entityRouteName: string): IAction => ({
  id: 'details',
  path: ':id/details',
  name: buildDetailsRouteName(entityRouteName),
  type: ActionType.LINK,
  label: () => 'entity.action.details',
  title: () => 'entity.action.details',
  class: () => '',
  icon: () => 'edit',
  iconColor: () => SeverityCodes.INFO,
  visible: (): boolean => true,
  execute: (row: object) => buildDetailsRoute(entityRouteName, row),
})

/**
 * Build the default Delete/Remove action
 *
 * @param entityRouteName The entity route name.
 */
export const buildRemoveAction = (entityRouteName: string): IAction => ({
  id: BasicAction.DELETE,
  path: 'remove',
  name: buildRemoveRouteName(entityRouteName),
  type: ActionType.BUTTON,
  label: () => 'entity.action.delete',
  title: () => 'entity.action.delete',
  class: () => '',
  icon: () => 'trash',
  iconColor: () => SeverityCodes.DANGER,
  visible: (): boolean => true,
  execute: (row?: object, emit?) => emit('remove', row),
})

/**
 * Return if the action is visible or not.
 *
 * @param action The action.
 * @param row
 * @returns {boolean|*} Return true or false.
 */
export const isActionVisible = (action: IAction, row?: object): boolean => {
  if (action?.visible) {
    return action.visible(row)
  }
  return false
}

/**
 * Return if the action is visible or not.
 *
 * @param entity The entity.
 * @returns {boolean|*} Return true or false.
 */
export const isActionsVisible = (entity: IEntity): boolean => {
  if (entity?.actionsVisible) {
    return entity.actionsVisible()
  }
  return entity.fields.length > 0
}

/**
 * Return if the action is visible or not.
 *
 * @param entity The entity.
 * @param row The table row.
 * @returns {boolean|*} Return true or false.
 */
export const isRowActionsVisible = (entity: IEntity, row?: object): boolean => {
  if (entity?.rowActions?.length) {
    const filtered: IAction[] = entity.rowActions.filter((action: IAction) => isActionVisible(action, row))
    return filtered.length !== 0 ? entity.rowActions.length > 0 : false
  }
  return false
}

/**
 * Return if the action is a Button, and it is visible.
 * @param action The action
 * @param row The row data
 */
export const isButtonVisible = (action: IAction, row?: object) => action.type === ActionType.BUTTON && isActionVisible(action, row)

/**
 * Return if the action is a Separator, and it is visible.
 * @param action The action
 * @param row The row data
 */
export const isSeparatorVisible = (action: IAction, row?: object) => action.type === ActionType.DIVIDER && isActionVisible(action, row)

/**
 * Return if the action is a Link, and it is visible.
 * @param action The action
 * @param row The row data
 */
export const isLinkVisible = (action: IAction, row?: object) => action.type === ActionType.LINK && isActionVisible(action, row)

/**
 * Return if the action is a Emmiter, and it is visible.
 * @param action The action
 * @param row The row data
 */
export const isEmitterVisible = (action: IAction, row?: object) => action.type === ActionType.EMITTER && isActionVisible(action, row)

/**
 * Return if the action is a Dropdown, and it is visible.
 * @param action The action
 * @param row The row data
 */
export const isDropdownVisible = (action: IAction, row?: object) => action.type === ActionType.DROPDOWN && isActionVisible(action, row)

/**
 * Filter properties by pattern.
 * @param list The list to filter.
 * @param pattern The patter filter.
 */
const baseFilter = (list: object[], pattern: string): object[] =>
  list.filter((obj: object) => {
    let found = false
    Object.values(obj).forEach((val: any) => {
      if (String(val).toLowerCase().indexOf(pattern.toLowerCase()) > -1) {
        found = true
      }
    })
    return found
  })

/**
 * Filter the list based on the passed pattern.
 * @param list The list to filter.
 * @param pattern The filter pattern.
 * @returns Array Return the elements matching the pattern.
 */
const filterList = (list: object[], pattern: string): object[] => (pattern?.length > 0 ? baseFilter(list, pattern) : list)

/**
 * Render the value if available using the custom render or default value in missing cases.
 * @param field The field specification object.
 * @param row The row object.
 * @returns {*|string}
 */
const renderValue = (row: object, field: IField): string => {
  if (field.render) return field.render(row, field)
  return row[field.name]
}

export interface IEntityTableEmit {
  (e: 'sort', propOrder: string, reverse: boolean): void

  (e: 'page', page: number): void

  (e: 'filter', filter: object): void

  (e: 'remove', row: any): void

  (e: 'selection', selected: object): void

  (e: 'custom', eventName: string, row: any): void
}

export default function useListEntity(entity: IEntity, emit?: IEntityTableEmit): any {
  if (!entity) {
    throw new Error('Entity is required!!')
  }
  const criteriaFilter = reactive<object>({})
  const primeDTFilter = reactive<object>({})
  const reverse = ref(entity?.reverse || false)
  const propOrder = ref(entity?.propOrder)
  const entityList = reactive([])
  const currentPage = ref(0)
  const totalCount = ref(0)
  const pageSize = getPageSize(entity.pageSize)
  const totalRows = computed(() => (pageSize ? totalCount.value : entityList.length))

  /**
   * Asc String Sorting comparator function.
   * @param current The current element.
   * @param next The next element.
   * @returns {number}
   */
  const ascSort = (current: object, next: object): number => {
    if (current[propOrder.value] < next[propOrder.value]) return -1
    if (current[propOrder.value] > next[propOrder.value]) return 1
    return 0
  }

  /**
   * Desc String Sorting comparator function.
   * @param current The current element.
   * @param next The next element.
   * @returns {number}
   */
  const descSort = (current: object, next: object): number => {
    if (current[propOrder.value] > next[propOrder.value]) return -1
    if (current[propOrder.value] < next[propOrder.value]) return 1
    return 0
  }

  /**
   * Local Sorting based in the direction.
   * @param list The list to sort.
   * @param direction The sorting direction.
   */
  const sortList = (list: object[], direction: string): object[] =>
    list.sort((current, next) => {
      if (direction) {
        return descSort(current, next)
      }
      return ascSort(current, next)
    })

  /**
   * Util function change the sorting order in specific column.
   * @param keyOrder The column order.
   */
  const changeOrder = (keyOrder: string): void => {
    propOrder.value = keyOrder
    reverse.value = !reverse.value
  }

  /**
   * Util function change the sorting order in specific column and emit the 'sort' event.
   * @param keyOrder The column order.
   */
  const emitSort = (keyOrder: string): void => {
    changeOrder(keyOrder)
    if (!emit) {
      throw new Error('Emit is required!!')
    }
    emit('sort', propOrder.value, reverse.value)
  }

  /**
   * Util function to emit the 'remove' event.
   * @param row The row.
   */
  const emitRemove = (row: object): void => {
    if (!emit) {
      throw new Error('Emit is required!!')
    }
    emit('remove', row)
  }

  /**
   * Emit custom event with te selected elements.
   * @returns {Object} The row object.
   */
  const emitCustom = (action: IAction, row: object): void => {
    if (!emit) {
      throw new Error('Emit is required!!')
    }
    emit('custom', action.id, row)
  }

  /**
   * Set sort order, default asc
   */
  const sort = () => [`${propOrder.value || 'id'},${reverse.value ? 'desc' : 'asc'}`]

  return {
    reverse,
    propOrder,
    entityList,
    pageSize,
    totalCount,
    currentPage,
    totalRows,
    criteriaFilter,
    primeDTFilter,
    sort,
    ascSort,
    descSort,
    sortList,
    filterList,
    changeOrder,
    emitSort,
    emitRemove,
    emitCustom,
    renderValue,
  }
}
