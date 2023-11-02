<template>
  <InsideOutDialog
    :header="props.entity?.headerLabel ? $t(props.entity.headerLabel) : ''"
    :show="visible"
    class="p-dialog-maximized"
    @hide="hideDialog"
    @unmaximize="hideDialog"
  >
    <template v-slot:[insideOut]>
      <DataTable
        ref="dt"
        v-model:editingRows="editingRows"
        v-model:expandedRows="expandedRowsResult"
        v-model:filters="filters"
        :class="`p-datatable-${entity?.datatableSize || DatatableSize.SMALL}`"
        :contextMenu="contextMenu"
        :filterDisplay="filterDisplay"
        :first="entity?.first || 0"
        :globalFilterFields="globalFilterFields"
        :lazy="serverPaging"
        :loading="loading"
        :paginator="!entity.dtDisablePaginator"
        :rows="entity?.pageSize || 15"
        :showGridlines="!entity.dtDisableGridlines"
        :stripedRows="stripedRows"
        :totalRecords="totalRecords"
        :value="resultItems"
        editMode="row"
        removableSort
        tableStyle="min-width: 50rem"
        v-bind="$attrs"
        @page="loadPage"
        @rowCollapse="onRowCollapse"
        @rowExpand="onRowExpand"
        @sort="loadSort"
        @row-contextmenu="onRowRightClick"
      >
        <Column v-if="entity.children" expander style="width: 2rem" />
        <Column
          v-for="col of fields"
          :key="col.id"
          :bodyStyle="{ 'text-align': col.textAlign || TextAlign.START }"
          :dataType="col.dataType || 'text'"
          :field="col.id"
          :header="getTranslation(col.header)"
          :sortField="col.sortField || col.id"
          :sortable="col.sortable"
          headerClass="column-text-right"
        >
          <template v-if="col?.render" #body="slotProps">
            <span
              v-tooltip.top="col.renderTooltipMessage ? $t(col.renderTooltipMessage) : ''"
              :class="col.renderClass"
              @click="e => onClickRender(e, slotProps, col)"
              v-html="col.render(slotProps.data, col)"
            ></span>
          </template>
          <template v-else #body="slotProps">
            <span @click="e => onClickCell(e, slotProps, col)" v-text="slotProps.data[slotProps.field]"></span>
          </template>
          <template v-if="col.filterType" #filter="{ filterModel, field }">
            <BaseInput
              v-if="col.filterType === FilterType.TEXT"
              v-model="filterModel.value"
              :placeholder="$t('components.dt.searchBy', { field: getTranslation(col.header) })"
              class="p-column-filter"
              type="text"
            />
            <span v-if="col.filterType === FilterType.BOOLEAN">
              <label :for="`${field}-filter`" class="mr-1 font-bold"> {{ field }} </label>
              <TriStateCheckbox v-model="filterModel.value" :inputId="`${field}-filter`" />
            </span>
            <BaseCalendar
              v-if="col.filterType === FilterType.DATE"
              v-model="filterModel.value"
              :dateFormat="dateTimeFormats.SHORT_DATE"
              :placeholder="dateTimeFormats.SHORT_DATE"
              mask="99/99/9999"
            />
            <BaseNumber v-if="col.filterType === FilterType.NUMERIC" v-model="filterModel.value" />

            <slot v-if="col.filterType === FilterType.SELECT" :data="filterModel" name="selectFilter">
              <BaseDropdown
                v-model="filterModel.value"
                :options="col.filterSelectOptions()"
                :placeholder="$t('components.dt.selectFilterPlaceholder')"
                class="p-column-filter"
                showClear
              >
                <template #option="slotProps">
                  <slot :data="slotProps.data" name="selectFilterOption">
                    <span v-if="col.renderFilterSelectOption" v-html="col.renderFilterSelectOption(slotProps.data, col)" />
                  </slot>
                </template>
              </BaseDropdown>
            </slot>

            <BaseMultiSelect
              v-if="col.filterType === FilterType.MULTISELECT"
              v-model="filterModel.value"
              :optionLabel="col.selectOptionLabel"
              :optionValue="col.selectOptionValue"
              :options="col.filterSelectOptions()"
              :placeholder="$t('components.dt.multiSelectFilterPlaceholder')"
              class="p-column-filter"
            >
              <template #option="slotProps">
                <slot :data="slotProps.data" name="multiSelectFilterOption">
                  <span v-if="col.renderFilterSelectOption" v-html="col.renderFilterSelectOption(slotProps.data, col)" />
                </slot>
              </template>
            </BaseMultiSelect>
          </template>
          <template #filterclear="{ filterCallback }">
            <BaseButton severity="secondary" type="button" @click="filterCallback()">
              <font-awesome-icon icon="times" />
            </BaseButton>
          </template>
          <template #filterapply="{ filterCallback }">
            <BaseButton severity="success" type="button" @click="filterCallback()">
              <font-awesome-icon icon="check" />
            </BaseButton>
          </template>
          <template v-if="entity.dtEditRowInPlace" #editor="{ data, field }">
            <slot :col="col" :data="data" :field="field" name="editRowInPlace">
              <span v-if="col?.render" v-html="col.render(data, col)"></span>
              <span v-else v-text="data[field]"></span>
            </slot>
          </template>
        </Column>
        <Column
          v-if="entity.dtEditRowInPlace"
          :exportable="false"
          :pt="{
            bodyCell: {
              style: 'width: 66px !important;',
            },
          }"
          :rowEditor="true"
        >
          <template #body="slotProps">
            <BaseButton
              v-if="slotProps.data.id !== currentEditRow.id"
              v-tooltip="$t('entity.action.edit')"
              icon="pencil"
              rounded
              severity="info"
              text
              @click="() => onRowEditInit(slotProps.data)"
            />
            <span>
              <BaseButton
                v-if="slotProps.data.id === currentEditRow.id"
                v-tooltip="$t('entity.action.save')"
                :icon="!isSaving ? 'check' : 'spinner'"
                :iconSpin="isSaving"
                class="block"
                rounded
                severity="success"
                text
                @click="() => onRowEditSave(slotProps.data)"
              />
              <BaseButton
                v-if="slotProps.data.id === currentEditRow.id"
                v-tooltip="$t('entity.action.cancel')"
                icon="times"
                rounded
                severity="danger"
                text
                @click="() => onRowEditCancel(slotProps.data)"
              />
            </span>
          </template>
        </Column>
        <Column
          v-if="!props.entity.dtNoRowActions"
          :bodyStyle="{ 'text-align': TextAlign.CENTER }"
          :exportable="false"
          :pt="{
            bodyCell: {
              style: 'width: 66px !important;',
            },
          }"
        >
          <template #body="slotProps">
            <RowActionMenu
              v-if="isRowActionsVisible(entity, slotProps.data)"
              :entity="entity"
              :row="slotProps.data"
              @custom="emitCustom"
              @remove="emitRemove"
            />
          </template>
        </Column>
        <!--Start Render for Header-->
        <template v-if="!entity.disableHeaderActions || !entity.disableGlobalFilter" #header>
          <div class="flex justify-content-between">
            <span v-if="entity.disableHeaderActions"></span>
            <span v-if="!entity.disableHeaderActions" class="p-buttonset">
              <BaseButton v-if="entity.dtAddRowInPlace" v-tooltip.top="$t('entity.action.add')" raised text @click="onAddRowInPlace">
                <font-awesome-icon :color="SeverityCodes.SUCCESS" icon="plus" />
              </BaseButton>
              <RenderActions
                v-for="(action, actionsIndex) in entity.leftToolsActions"
                :key="actionsIndex"
                :action="action"
                :entity="entity"
                :printLabel="false"
                :raised-property="true"
                :row="currentRow"
                @custom="emitCustom"
                @remove="emitRemove"
              />
              <BaseButton v-if="canExpand" v-tooltip.top="$t('components.dt.maximizable')" raised text @click="onMaximize">
                <font-awesome-icon icon="maximize" />
              </BaseButton>
              <BaseButton v-if="entity.exportCSV" v-tooltip.top="$t('components.dt.exportCSV')" raised text @click="exportCSV">
                <font-awesome-icon icon="external-link" />
              </BaseButton>
              <BaseButton v-if="entity.filterDisplay" v-tooltip.top="$t('components.dt.restartFilter')" raised text @click="restartFilter">
                <font-awesome-icon :color="SeverityCodes.DANGER" icon="filter-circle-xmark" />
              </BaseButton>
              <h5
                v-if="entity.dtAddRowInPlace && entity.childrenHeaderLabelRender"
                class="inline m-4"
                v-html="entity.childrenHeaderLabelRender(treeFather)"
              ></h5>
            </span>
            <span v-if="!entity.disableGlobalFilter" class="p-input-icon-left">
              <font-awesome-icon icon="search" />
              <BaseInput
                id="dt_search"
                v-model="filters['global'].value"
                :placeholder="$t(entity.searchPlaceHolder)"
                :style="{ width: '20vw' }"
              />
            </span>
          </div>
        </template>
        <!--End Render for Header-->
        <!--Start Render for empty-->
        <template #empty>
          <div class="flex justify-content-center" v-text="$t('info.noInformation')" />
        </template>
        <!--End Render for empty-->
        <template v-if="entity.children" #expansion="slotProps">
          <slot :data="slotProps.data" name="expansionDT" />
        </template>
      </DataTable>
    </template>
  </InsideOutDialog>
  <ContextMenu ref="routeMenu" :model="entity.rowActions" style="width: auto">
    <template #item="{ item: action }">
      <RenderActions :action="action" :entity="entity" :row="currentRow" icon-class="px-2" @custom="emitCustom" @remove="emitRemove" />
    </template>
  </ContextMenu>
</template>

<script lang="ts" setup>
import { computed, onMounted, onUnmounted, PropType, ref, Ref, toRaw, watch } from 'vue'
import DataTable, { DataTableRowContextMenuEvent } from 'primevue/datatable'
import BaseButton from '@/shared/components/ui/buttons/BaseButton.vue'
import InsideOutDialog from '@/shared/components/ui/dialogs/InsideOutDialog.vue'
import BaseDropdown from '@/shared/components/ui/form/BaseDropdown.vue'
import BaseCalendar from '@/shared/components/ui/form/BaseCalendar.vue'
import BaseMultiSelect from '@/shared/components/ui/form/BaseMultiSelect.vue'
import BaseNumber from '@/shared/components/ui/form/BaseNumber.vue'
import Column from 'primevue/column'
import TriStateCheckbox from 'primevue/tristatecheckbox'
import ContextMenu from 'primevue/contextmenu'
import RowActionMenu from './RowActionMenu'
import { IAction, IEntity, IField, IFilter } from '@/views/entities/common/entity-interface'
import { useI18n } from 'vue-i18n'
import { initFilter, isRowActionsVisible } from '@/views/entities/common/entity-api'
import object from '@/shared/api/object'
import { ActionType, DatatableSize, EditingStatus, FilterType, SeverityCodes, TextAlign } from '@/shared/const/entity-constants'
import { useRouter } from 'vue-router'
import RenderActions from './RenderActions'
import BaseInput from '@/shared/components/ui/form/BaseInput.vue'
import { dateTimeFormats } from '@/shared/const/global-constants'
import { useCustomToast } from '@/shared/composables/custom-toast'
import useModel from '@/shared/composables/use-model'
import { useDataTableStore } from '@/store/modules/datatable/datatable-store'

const emit = defineEmits([
  'filter',
  'remove',
  'custom',
  'page',
  'click-render',
  'click-cell',
  'sort',
  'rowExpand',
  'rowCollapse',
  'row-add-init',
  'row-edit-init',
  'row-edit-save',
  'row-edit-cancel',
  'update:expandedRows',
  'update:editingStatus',
])
const props = defineProps({
  entity: {
    type: Object as PropType<IEntity>,
    required: true,
  },
  expandedRows: {
    type: Array,
  },
  items: {
    type: Array as PropType<object[]>,
    default: () => [],
  },
  totalRecords: {
    type: Number as PropType<number>,
    default: 0,
  },
  initialFilter: {
    // This render the initial filter.
    type: object as PropType<IFilter>,
    default: () => {},
  },
  addInRowObject: {
    type: Object,
    default: () => {},
  },
  contextMenu: {
    type: Boolean,
    default: false,
  },
  stripedRows: {
    type: Boolean,
    default: false,
  },
  filterDisplay: {
    type: String,
    default: '',
  },
  treeFather: {
    type: Object,
    default: () => {},
  },
  editingStatus: {
    type: String as PropType<EditingStatus>,
    default: EditingStatus.NONE,
  },
})

const i18n = useI18n()
const { t } = useI18n()
const customToast = useCustomToast()
const router = useRouter()
const dt = ref()
const routeMenu = ref()
const isSaving = ref(false)
const editingRows = ref([])
const currentEditRow = ref({})
const currentAddRow = ref()
const currentRow = ref({})
const loading: Ref<boolean> = ref(false)
const filters: Ref<IFilter> = ref(initFilter(props.entity.filters, props.initialFilter))
const fields = computed<IField[]>(() => props.entity.fields)
const serverPaging = computed<boolean>(() => props.entity.serverPaging)
const globalFilterFields = computed<string[]>(() => (!serverPaging.value ? props.entity.globalFilterFields : []))
const insideOut = ref('out')
const visible = ref(false)
const canExpand = ref(false)
const { model: expandedRowsResult } = useModel(props, emit, { valueName: 'expandedRows' })

const dataTableStore = useDataTableStore()

onMounted(() => {
  canExpand.value = props.entity.dtMaximizableModal
})
onUnmounted(() => {
  dataTableStore.resetDatatableState()
})

watch(
  () => filters.value,
  () => {
    emit('filter', filters.value)
  },
  { deep: true, immediate: true }
)

watch(
  () => props.editingStatus,
  () => {
    switch (props.editingStatus) {
      case EditingStatus.SAVED:
        addRowInPlaceReset()
        break
      case EditingStatus.ERROR:
        isSaving.value = false
        break
      case EditingStatus.SAVING:
        isSaving.value = true
        break
      default:
        isSaving.value = false
    }
  }
)

const onRowExpand = info => {
  emit('rowExpand', info)
}

const onRowCollapse = info => {
  addRowInPlaceReset()
  emit('rowCollapse', info)
}

const emitCustom = info => {
  emit('custom', info)
}

const emitRemove = info => {
  emit('remove', info)
}

const onClickRender = (event, data, col) => {
  emit('click-render', { originalEvent: event, ...data, conf: col })
}

const onClickCell = (event, data, col) => {
  emit('click-cell', { originalEvent: event, ...data, conf: col })
}

const loadPage = (info): void => {
  if (props.entity.serverPaging) {
    emit('page', info)
  }
}

const addRowInPlaceReset = () => {
  dataTableStore.setIsEditing(false)
  currentEditRow.value = {}
  currentAddRow.value = null
  editingRows.value = []
  isSaving.value = false
}

const loadSort = (info): void => {
  emit('sort', info)
}

const onRowEditInit = (info): void => {
  if (cantEditRow()) {
    addRowInPlaceReset()
    dataTableStore.setIsEditing(true)
    currentEditRow.value = info
    editingRows.value = [info]
    emit('row-edit-init', { newData: info })
  }
}

const onRowEditSave = (info): void => {
  emit('row-edit-save', { newData: info })
}

const onRowEditCancel = (info): void => {
  addRowInPlaceReset()
  emit('row-edit-cancel', { newData: info })
}

const onMaximize = () => {
  insideOut.value = 'inside'
  visible.value = true
  canExpand.value = false
}

const hideDialog = () => {
  insideOut.value = 'out'
  visible.value = false
  canExpand.value = true
}

const cantEditRow = (): boolean => {
  if (dataTableStore.getIsEditing) {
    customToast.warnToast(t('components.dt.editOneRow'))
    return false
  }
  emit('update:editingStatus', EditingStatus.NONE)
  return true
}

const rawRowObject = computed(() => toRaw(props.addInRowObject))

const onAddRowInPlace = () => {
  if (cantEditRow()) {
    addRowInPlaceReset()
    dataTableStore.setIsEditing(true)
    editingRows.value = [rawRowObject.value]
    currentAddRow.value = rawRowObject.value
    emit('row-add-init', props.treeFather)
  }
}

const resultItems = computed(() => {
  const copyList = [...props.items]
  if (props.entity.dtAddRowInPlace && currentAddRow.value) {
    copyList.splice(0, 0, currentAddRow.value)
  }
  return copyList
})

/**
 * Translate fields key.
 * @param key The lang key
 */
const getTranslation = (key: string) => i18n.t(key)

/**
 * Emit custom event with te selected element.
 * @returns {Object} The row object.
 */
const executeAction = (action: IAction): void => {
  if (action.type === ActionType.EMITTER) {
    emitCustom(action.id)
    return
  }
  if (action?.execute) action.execute(currentRow.value, emit) // Pass the field empty.
  currentRow.value = {}
}

const navigateToLink = (action: IAction, row = {}) => {
  router.push(action.execute(row))
  currentRow.value = {}
}

const exportCSV = () => {
  dt.value.exportCSV()
}

const onRowRightClick = (rowRightClickEvent: DataTableRowContextMenuEvent) => {
  currentRow.value = { ...rowRightClickEvent.data }
  routeMenu.value.show(rowRightClickEvent.originalEvent)
}

const restartFilter = () => {
  filters.value = initFilter(props.entity.filters, props.initialFilter)
}
</script>

<style lang="scss">
.column-text-right {
  .p-column-header-content {
    .p-column-title {
      margin: auto; // or center
      display: block !important;
      width: 80%;
      text-align: center;
    }
  }
}
</style>
