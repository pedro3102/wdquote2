<template>
  <BaseDataTable
    :stripedRows="true"
    :contextMenu="true"
    :filterDisplay="entity.filterDisplay"
    :items="items"
    :entity="entity"
    :totalRecords="totalCount"
    :addInRowObject="addInRowObject"
    :treeFather="treeFather"
    v-model:expandedRows="expandedRows"
    v-model:editingStatus="editingStatusResult"
    @filter="applyFilter"
    @remove="remove"
    @custom="emitCustom"
    @page="loadPage"
    @click-render="onClickRender"
    @sort="loadSort"
    @rowExpand="onRowExpand"
    @rowCollapse="onRowCollapse"
    @row-edit-init="onRowEditInit"
    @row-add-init="onRowAddInit"
    @row-edit-save="onRowEditSave"
    @row-edit-cancel="onRowEditCancel"
  >
    <template #expansionDT="slotProps">
      <slot name="expansionDT" :data="slotProps.data" />
    </template>
    <template #multiSelectFilterOption="slotProps">
      <slot name="multiSelectFilterOption" :data="slotProps.data" />
    </template>
    <template #selectFilterOption="slotProps">
      <slot name="selectFilterOption" :data="slotProps.data" />
    </template>
    <template #selectFilter="filterModel">
      <slot name="selectFilter" :data="filterModel.data" />
    </template>
    <template #editRowInPlace="{ data, field, col }">
      <slot name="editRowInPlace" :data="data" :field="field" :col="col" />
    </template>
  </BaseDataTable>
  <!--  :sortMode="entity.sortMode || SortModeType.SINGLE"-->
</template>

<script setup lang="ts" generic="T">
import { PropType, computed, onMounted, watch, ref } from 'vue'
import BaseDataTable from '@/shared/components/ui/datatable/BaseDataTable.vue'
import { IEntity, IFilter } from '@/views/entities/common/entity-interface'
import { useRoute } from 'vue-router'
import useListEntity, { toCriteriaFilterObject } from '@/views/entities/common/entity-api'
import { useCustomToast } from '@/shared/composables/custom-toast'
import { useI18n } from 'vue-i18n'
import { buildListRouteName } from '@/shared/config/router/router-utils'
import { EditingStatus, RenderType } from '@/shared/const/entity-constants'
import { useCustomConfirm } from '@/shared/composables/custom-confirm'
import useModel from '@/shared/composables/use-model'

const emit = defineEmits([
  'custom',
  'click-render',
  'rowExpand',
  'rowCollapse',
  'row-add-init',
  'row-edit-init',
  'row-edit-save',
  'row-edit-cancel',
  'items-loaded',
  'update:editingStatus',
])
const props = defineProps({
  entity: {
    type: Object as PropType<IEntity>,
    required: true,
  },
  service: {
    type: Object as PropType<any>,
    required: true,
  },
  staticFilters: {
    type: Function,
  },
  onBeforeRowLoad: {
    type: Function,
  },
  editingStatus: {
    type: String as PropType<EditingStatus>,
    default: EditingStatus.NONE,
  },
  addInRowObject: {
    type: Object,
    default: () => {},
  },
  treeFather: {
    type: Object,
    default: () => {},
  },
})

const route = useRoute()
const { t } = useI18n()
const confirm = useCustomConfirm()
const customToast = useCustomToast()
let { criteriaFilter, entityList, currentPage, totalCount, pageSize, sort, propOrder, reverse } = useListEntity(props.entity)
const items = computed<T[]>(() => entityList)
const lastFilter = ref({})
const expandedRows = ref([])
const dialogGroup = 'general'
const { model: editingStatusResult } = useModel(props, emit, { valueName: 'editingStatus' })

onMounted(() => {
  if (!props.entity.serverPaging) {
    loadItems()
  }
})

watch(
  () => route.name,
  () => {
    if (
      route.name === props.entity.name ||
      route.name === buildListRouteName(props.entity.name) ||
      route.name === props.entity.watchOnRouteName
    ) {
      if (props.entity.serverPaging) {
        applyFilter(lastFilter.value)
      } else {
        loadItems()
      }
    }
  }
)

watch(
  () => props.editingStatus,
  () => {
    if (props.editingStatus === EditingStatus.SAVED) {
      loadItems()
    }
  }
)

/**
 * Load the remote items using pagination.
 */
const loadItems = async (): Promise<void> => {
  try {
    if (props.staticFilters) {
      Object.assign(criteriaFilter, toCriteriaFilterObject(props.staticFilters()))
    }
    const queryParams = {
      ...criteriaFilter,
      page: currentPage.value,
      size: pageSize,
      sort: sort(),
    }
    const url = props.service.buildQuery(queryParams)
    const { data, xTotalCount } = await props.service.list(url)
    if (data) {
      entityList.splice(0)
      totalCount.value = xTotalCount
      data.forEach(item => {
        let toPush = { ...item }
        if (props.entity.onBeforeRowLoad) {
          toPush = props.entity.onBeforeRowLoad(item)
        }
        if (props.onBeforeRowLoad) {
          toPush = props.onBeforeRowLoad(item)
        }
        if (toPush.defaultExpanded) {
          delete toPush.defaultExpanded
          expandedRows.value.push(toPush)
        }
        entityList.push(toPush)
      })
      emit('items-loaded', entityList)
    }
  } catch (exception) {
    if ((exception?.response, exception?.response?.status)) {
      await customToast.responseErrorToast(exception.response.status)
    }
  }
}

const applyFilter = (newFilter: IFilter) => {
  lastFilter.value = { ...newFilter }
  criteriaFilter = {}
  if (props.entity.serverPaging) {
    Object.assign(criteriaFilter, toCriteriaFilterObject(newFilter))
    loadItems()
  }
}

const loadPage = pageEvent => {
  currentPage.value = pageEvent.page
  applyFilter(pageEvent.filters)
}

const loadSort = sortEvent => {
  propOrder.value = sortEvent.sortField
  reverse.value = sortEvent.sortOrder === -1
  applyFilter(sortEvent.filters)
}

/**
 * Remove item.
 */
const acceptRemove = (row: any) => {
  const removeProperty = props.entity.keyBackProperty || 'id'
  props.service.remove(row[removeProperty]).then(() => {
    customToast.successToast('entity.delete.success')
    loadItems()
  })
}

const rejectRemove = () => {}

const remove = async (row: any): Promise<void> => {
  const rowValue = props.entity.deleteMsgField ? props.entity.deleteMsgField(row) : row.id
  confirm.openDeleteConfirm(dialogGroup, rowValue, () => acceptRemove(row), rejectRemove)
}

const emitCustom = info => {
  emit('custom', info)
}

const onRowExpand = info => {
  emit('rowExpand', info)
}

const onRowCollapse = info => {
  emit('rowCollapse', info)
}

const onRowEditInit = (info): void => {
  emit('row-edit-init', info)
}

const onRowAddInit = (info): void => {
  emit('row-add-init', info)
}

const onRowEditSave = (info): void => {
  emit('row-edit-save', info)
}

const onRowEditCancel = (info): void => {
  emit('row-edit-cancel', info)
}

const acceptChangeStatus = event => {
  const updateProperty = props.entity.keyBackProperty || 'id'
  props.service[event.conf.changeStatusServName]({ [updateProperty]: event.data[updateProperty] }).then(() => {
    customToast.successToast('entity.action.changeStatus.success')
    loadItems()
  })
}

const rejectChangeStatus = () => {
  customToast.errorToast('entity.action.changeStatus.cancel')
}

const onClickRender = event => {
  if (event.conf?.renderType === RenderType.BOOLEAN && event.conf.changeStatusAlertLabel) {
    confirm.openChangeBooleanStatusConfirm(
      dialogGroup,
      event.data[event.conf.changeStatusAlertLabel],
      () => acceptChangeStatus(event),
      rejectChangeStatus
    )
  } else {
    emit('click-render', event)
  }
}
</script>
