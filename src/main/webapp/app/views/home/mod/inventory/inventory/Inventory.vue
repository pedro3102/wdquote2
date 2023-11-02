<template>
  <router-view />
  <DataTableList
    v-model:editingStatus="editingStatus"
    :entity="inventoryEntity"
    :service="InventoryService"
    @row-edit-init="onRowEditInit"
    @row-edit-save="onRowEditSave"
  >
    <template #editRowInPlace="{ data, field }">
      <FormGroupNumber
        v-if="field === 'reorderPoint'"
        id="field_reorderPoint"
        v-model="inventory.reorderPoint"
        :label="$t('inventory.reorderPoint')"
        :maxFractionDigits="2"
        :rules="validator.inventory.reorderPoint"
        name="field_reorderPoint"
      >
      </FormGroupNumber>
      <FormGroupNumber
        v-if="field === 'vendorLeadTime'"
        id="field_vendorLeadTime"
        v-model="inventory.vendorLeadTime"
        :label="$t('inventory.vendorLeadTime')"
        :rules="validator.inventory.vendorLeadTime"
        name="field_vendorLeadTime"
      >
      </FormGroupNumber>
      <FormGroupInput
        v-if="field === 'shelf'"
        id="field_shelf"
        v-model="inventory.shelf"
        :label="$t('inventory.shelf')"
        :rules="validator.inventory.shelf"
        name="field_shelf"
      >
      </FormGroupInput>
    </template>
  </DataTableList>
</template>

<script lang="ts" setup>
import DataTableList from '@/shared/components/ui/datatable/DataTableList.vue'
import { inventoryEntity } from './inventory-entity'
import InventoryService from './inventory-service'
import FormGroupInput from '@/shared/components/ui/form/FormGroupInput.vue'
import FormGroupNumber from '@/shared/components/ui/form/FormGroupNumber.vue'
import { useVuelidate } from '@vuelidate/core'
import { DataTableRowEditSaveEvent } from 'primevue/datatable'
import { ref, reactive, toRaw, Ref } from 'vue'
import { IInventory, Inventory } from '@/views/home/mod/inventory/inventory/inventory-model'
import inventoryRules from '@/views/home/mod/inventory/inventory/inventory-rules'
import { useI18n } from 'vue-i18n'
import { useCustomToast } from '@/shared/composables/custom-toast'
import { EditingStatus } from '@/shared/const/entity-constants'

const { t } = useI18n()
const customToast = useCustomToast()
const inventory: IInventory = reactive(new Inventory())
const validator = useVuelidate(inventoryRules, { inventory })
const editingStatus: Ref<EditingStatus> = ref(EditingStatus.NONE)

const onRowEditSave = (info: DataTableRowEditSaveEvent): void => {
  // Object.assign(inventory, info.newData)

  if (!validator.value.inventory.$invalid) {
    editingStatus.value = EditingStatus.SAVING
    InventoryService.patch(toRaw(inventory))
      .then(() => {
        editingStatus.value = EditingStatus.SAVED
        customToast.successToast(t('inventory.actions.update.success'))
      })
      .catch(() => {
        editingStatus.value = EditingStatus.ERROR
        customToast.errorToast(t('inventory.actions.update.error'))
      })
  } else {
    customToast.errorToast(t('inventory.actions.update.invalid'))
  }
}

const onRowEditInit = (info): void => {
  Object.assign(inventory, info.newData)
}
</script>
