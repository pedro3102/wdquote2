<template>
  <!--  We add this for /new /:id/edit /:id/delete child routes-->
  <router-view @conversion-updated="onUnitOfMeasureConversionUpdated" />
  <DataTableList :entity="unitOfMeasureEntity" :service="UnitOfMeasureService" :onBeforeRowLoad="onBeforeUOMRowLoad">
    <template #expansionDT="slotProps">
      <DataTableList
        :entity="unitOfMeasureConversionEntity"
        :service="UnitOfMeasureConversionService"
        :staticFilters="() => buildStaticFilter(slotProps.data)"
        :addInRowObject="unitOfMeasureConversion"
        :treeFather="slotProps.data"
        v-model:editingStatus="editingStatus"
        @row-edit-init="onConversionRowEditInit"
        @row-add-init="onConversionRowAddInit"
        @row-edit-save="onConversionRowEditSave"
        @row-edit-cancel="onConversionRowEditCancel"
      >
        <template #editRowInPlace="{ field, data }">
          <FormGroupDropdown
            v-if="field === 'uom'"
            id="field_unitOfMeasure"
            v-model="unitOfMeasureConversion.uom"
            name="field_unitOfMeasure"
            :disabled="true"
            :label="$t('unitOfMeasure.detail.title')"
            :options="[data[field]]"
            optionLabel="name"
          >
          </FormGroupDropdown>
          <FormGroupNumber
            v-if="field === 'conversionFactor'"
            id="field_conversionFactor"
            v-model="unitOfMeasureConversion.conversionFactor"
            name="field_conversionFactor"
            :maxFractionDigits="appConstants.APP.MAX_FRACTION_DIGITS"
            :label="$t('unitOfMeasureConversion.conversionFactor')"
            :rules="validator.unitOfMeasureConversion.conversionFactor"
          >
          </FormGroupNumber>
          <FormGroupDropdown
            v-if="field === 'uomEquivalent'"
            id="field_uomEquivalent"
            v-model="unitOfMeasureConversion.uomEquivalent"
            name="field_unitOfMeasure"
            :label="$t('unitOfMeasureConversion.uomEquivalent')"
            :options="uoms"
            optionLabel="name"
            :rules="validator.unitOfMeasureConversion.uomEquivalent"
          >
          </FormGroupDropdown>
        </template>
      </DataTableList>
    </template>
  </DataTableList>
</template>

)

<script lang="ts" setup>
import DataTableList from '@/shared/components/ui/datatable/DataTableList.vue'
import { unitOfMeasureEntity } from './unit-of-measure-entity'
import { unitOfMeasureConversionEntity } from '@/views/home/mod/inventory/unit-of-measure-conversion/unit-of-measure-conversion-entity'
import UnitOfMeasureService from './unit-of-measure-service'
import UnitOfMeasureConversionService from '@/views/home/mod/inventory/unit-of-measure-conversion/unit-of-measure-conversion-service'
import { Ref, ref, reactive, toRaw } from 'vue'
import { EditingStatus, FilterMatchMode, FilterOperatorOptions } from '@/shared/const/entity-constants'
import { IFilter } from '@/views/entities/common/entity-interface'
import { IUnitOfMeasure, UnitOfMeasure } from '@/views/home/mod/inventory/unit-of-measure/unit-of-measure-model'
import { useVuelidate } from '@vuelidate/core'
import unitOfMeasureConversionRules from '@/views/home/mod/inventory/unit-of-measure-conversion/unit-of-measure-conversion-rules'
import {
  IUnitOfMeasureConversion,
  UnitOfMeasureConversion,
} from '@/views/home/mod/inventory/unit-of-measure-conversion/unit-of-measure-conversion-model'
import { useCustomToast } from '@/shared/composables/custom-toast'
import { useI18n } from 'vue-i18n'
import FormGroupDropdown from '@/shared/components/ui/form/FormGroupDropdown.vue'
import FormGroupNumber from '@/shared/components/ui/form/FormGroupNumber.vue'
import appConstants from '@/shared/const/app-constants'

const { t } = useI18n()
const staticFilters = ref({})
const unitOfMeasureUpdated: Ref<IUnitOfMeasure> = ref({})
const editingStatus: Ref<EditingStatus> = ref(EditingStatus.NONE)
const unitOfMeasureConversion: IUnitOfMeasureConversion = reactive(new UnitOfMeasureConversion())
const validator = useVuelidate(unitOfMeasureConversionRules, { unitOfMeasureConversion })
const customToast = useCustomToast()
const uoms: IUnitOfMeasure[] = reactive([])

const loadUOMs = () => {
  UnitOfMeasureService.list(UnitOfMeasureService.baseUrl).then(res => {
    Object.assign(uoms, res.data)
  })
}
const buildStaticFilter = (uom: IUnitOfMeasure): IFilter => {
  staticFilters.value = {
    uomId: {
      operator: FilterOperatorOptions.AND,
      constraints: [{ value: uom.id, matchMode: FilterMatchMode.EQUALS }],
    },
  }
  return staticFilters.value
}
const onBeforeUOMRowLoad = (row: IUnitOfMeasure): IUnitOfMeasure => {
  const copy: IUnitOfMeasure = row
  if (row.id === unitOfMeasureUpdated.value.id) {
    copy.defaultExpanded = true
  }
  return copy
}

const restartConversionModel = () => {
  Object.assign(unitOfMeasureConversion, new UnitOfMeasureConversion())
}

const onConversionRowEditSave = (): void => {
  if (validator.value.unitOfMeasureConversion.$invalid) {
    customToast.errorToast(t('error.invalidForm'))
    return
  }
  if (unitOfMeasureConversion.id) {
    UnitOfMeasureConversionService.update(toRaw(unitOfMeasureConversion), unitOfMeasureConversion.id)
      .then(() => {
        restartConversionModel()
        editingStatus.value = EditingStatus.SAVED
        customToast.successToast(t('unitOfMeasure.actions.update.success'))
      })
      .catch(() => {
        editingStatus.value = EditingStatus.ERROR
        customToast.errorToast(t('unitOfMeasure.actions.update.error'))
      })
  } else {
    UnitOfMeasureConversionService.save(toRaw(unitOfMeasureConversion))
      .then(() => {
        restartConversionModel()
        editingStatus.value = EditingStatus.SAVED
        customToast.successToast(t('unitOfMeasure.actions.save.success'))
      })
      .catch(() => {
        editingStatus.value = EditingStatus.ERROR
        customToast.errorToast(t('unitOfMeasure.actions.save.error'))
      })
  }
}

const onUnitOfMeasureConversionUpdated = (data: IUnitOfMeasure): void => {
  unitOfMeasureUpdated.value = data
  editingStatus.value = EditingStatus.SAVED
}

const onConversionRowEditInit = (info): void => {
  loadUOMs()
  restartConversionModel()
  Object.assign(unitOfMeasureConversion, info.newData)
}

const onConversionRowAddInit = (unitOfMeasure): void => {
  loadUOMs()
  Object.assign(unitOfMeasureConversion, new UnitOfMeasureConversion())
  unitOfMeasureConversion.uom = {}
  Object.assign(unitOfMeasureConversion.uom, { ...unitOfMeasure })
}

const onConversionRowEditCancel = (): void => {
  restartConversionModel()
}
</script>
