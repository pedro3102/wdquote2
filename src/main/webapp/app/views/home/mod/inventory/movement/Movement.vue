<template>
  <!--  We add this for /new /:id/edit /:id/delete child routes-->
  <router-view/>
  <DataTableList v-model:editingStatus="editingStatusMovement" :entity="movementEntity" :service="MovementService"
                 @custom="confirmMovement">
    <template #expansionDT="slotProps">
      <DataTableList
        v-model:editingStatus="editingStatusDetail"
        :addInRowObject="movementDetail"
        :entity="movementDetailEntity"
        :service="MovementDetailService"
        :staticFilters="() => buildStaticFilter(slotProps.data)"
        :treeFather="slotProps.data"
        @row-edit-init="onDetailRowEditInit"
        @row-add-init="onDetailRowAddInit"
        @row-edit-save="onDetailRowEditSave"
        @row-edit-cancel="onDetailRowEditCancel"
        @items-loaded="onItemsLoaded">
        <template #editRowInPlace="{ field }">
          <FormGroupAutoComplete
            v-if="field === 'product.code'"
            id="field_product"
            v-model="movementDetail.product"
            :delay="500"
            :label="$t('movementDetail.product')"
            :minLength="2"
            :rules="validator.movementDetail.product"
            :suggestions="listOption.products"
            forceSelection
            name="field_product"
            optionLabel="code"
            @complete="loadProducts">
            <template #header="slotProps">
              <div class="flex align-items-center font-bold pl-3">
                <div style="width: 60px">{{ $t('product.code') }}</div>
                <div style="width: 200px">{{ $t('product.description') }}</div>
                <div v-if="currentMovement.type === 'OUT'">{{ $t('movementDetail.qty') }}</div>
              </div>
            </template>
            <template #option="slotProps">
              <div class="flex align-items-center">
                <div style="width: 60px">{{ slotProps.data.option.code }}</div>
                <div style="width: 200px;overflow: hidden;text-overflow: ellipsis;">
                  {{ slotProps.data.option.description }}
                </div>
                <div v-if="currentMovement.type === 'OUT'">{{ slotProps.data.option.qty }}</div>
              </div>
            </template>
          </FormGroupAutoComplete>
          <FormGroupInput
            v-if="field === 'vendorCode' && currentMovement.type === 'IN'"
            id="field_vendorCode"
            v-model="movementDetail.vendorCode"
            :label="$t('movementDetail.vendorCode')"
            name="field_vendorCode">
          </FormGroupInput>
          <FormGroupNumber
            v-if="field === 'unitCost' && currentMovement.type === 'IN'"
            id="field_unitCost"
            v-model="movementDetail.unitCost"
            :label="$t('movementDetail.unitCost')"
            :maxFractionDigits="5"
            :rules="validator.movementDetail.unitCost"
            name="field_unitCost">
          </FormGroupNumber>
          <FormGroupNumber
            v-if="field === 'salePrice'"
            id="field_salePrice"
            v-model="movementDetail.salePrice"
            :label="$t('movementDetail.salePrice')"
            :maxFractionDigits="2"
            :rules="validator.movementDetail.salePrice"
            name="field_salePrice">
          </FormGroupNumber>
          <FormGroupNumber
            v-if="field === 'qty'"
            id="field_qty"
            v-model="movementDetail.qty"
            :label="$t('movementDetail.qty')"
            :maxFractionDigits="5"
            :rules="validator.movementDetail.qty"
            name="field_qty">
          </FormGroupNumber>
          <FormGroupDropdown
            v-if="field === 'stockPosition.name' && currentMovement.type === 'IN'"
            id="field_stockPosition"
            v-model="movementDetail.stockPosition"
            :label="$t('movementDetail.stockPosition')"
            :options="listOption.stockPositions"
            name="field_stockPosition"
            optionLabel="name">
          </FormGroupDropdown>
        </template>
      </DataTableList>
    </template>
  </DataTableList>
</template>

<script lang="ts" setup>
import DataTableList from '@/shared/components/ui/datatable/DataTableList.vue'
import {movementEntity} from './movement-entity'
import {movementDetailEntity} from '@/views/home/mod/inventory/movement-detail/movement-detail-entity'
import FormGroupInput from '@/shared/components/ui/form/FormGroupInput.vue'
import FormGroupNumber from '@/shared/components/ui/form/FormGroupNumber.vue'
import FormGroupDropdown from '@/shared/components/ui/form/FormGroupDropdown.vue'
import FormGroupAutoComplete from '@/shared/components/ui/form/FormGroupAutoComplete.vue'
import MovementService from './movement-service'
import movementService from './movement-service'
import MovementDetailService from '@/views/home/mod/inventory/movement-detail/movement-detail-service'
import {IFilter} from '@/views/entities/common/entity-interface'
import {EditingStatus, FilterMatchMode, FilterOperatorOptions, OperationType} from '@/shared/const/entity-constants'
import {IMovement, Movement} from '@/views/home/mod/inventory/movement/movement-model'
import {reactive, ref, Ref, toRaw, watch} from 'vue'
import {IMovementDetail, MovementDetail} from '@/views/home/mod/inventory/movement-detail/movement-detail-model'
import {useVuelidate} from '@vuelidate/core'
import movementDetailRules from '@/views/home/mod/inventory/movement-detail/movement-detail-rules'
import {useCustomToast} from '@/shared/composables/custom-toast'
import {useI18n} from 'vue-i18n'
import ProductService from "@/views/home/mod/inventory/product/product-service";
import InventoryService from "@/views/home/mod/inventory/inventory/inventory-service";
import StockPositionService from "@/views/home/mod/inventory/stock-position/stock-position-service";
import {IProduct} from "@/views/home/mod/inventory/product/product-model";
import {IInventory} from "@/views/home/mod/inventory/inventory/inventory-model";
import {useCustomConfirm} from "@/shared/composables/custom-confirm";
import TranslationService from "@/locale/translation-service";
import EntityService from "@/views/entities/common/entity-service";

const {t} = useI18n()
const staticFilters = ref({})
const movementDetail: IMovementDetail = reactive(new MovementDetail())
const validator = useVuelidate(movementDetailRules, {movementDetail})
const customToast = useCustomToast()
const confirm = useCustomConfirm()
const editingStatusDetail: Ref<EditingStatus> = ref(EditingStatus.NONE)
const editingStatusMovement: Ref<EditingStatus> = ref(EditingStatus.NONE)
const listOption: any = reactive({
  inventories: [],
  products: [],
  stockPositions: [],
  productsInMovement: [],
})
const currentMovement: IMovement = reactive({})

watch(() => movementDetail.product, (product: IProduct) => {
  if (currentMovement.id && product && currentMovement.type === OperationType.OUT) {
    const inventory = listOption.inventories.find((i: IInventory) => i.product.id === product.id)
    if (inventory != undefined) {
      movementDetail.unitCost = inventory.unitCost
      movementDetail.inventory = inventory
    }
  }
})

const loadProducts = event => {
  function buildProductUrl(): string {
    const queryParams = {
      'codeDescription.contains': event.query,
      sort: 'code',
    }
    return EntityService.addQueryParams(ProductService.baseUrl + '/basic', queryParams)
  }

  function buildInventoryUrl(locationId: number): string {
    const queryParams = {
      'product.contains': event.query,
      'locationId.equals': locationId,
      'qty.greaterThan': 0,
      sort: 'product.code',
    }
    return InventoryService.buildQuery(queryParams)
  }

  if (currentMovement.type === OperationType.IN) { //load from products
    const url = buildProductUrl()
    ProductService.list(url).then(res => {
      const filterProductos = res.data.filter(p => !listOption.productsInMovement.some(e => p.id === e.id));
      listOption.products = [...filterProductos]
    })
  } else { // load from inventory
    const url = buildInventoryUrl(currentMovement.location.id)
    InventoryService.list(url).then(res => {
      listOption.inventories = [...res.data]
      const productsData = res.data.map(inventory => ({...inventory.product, qty: inventory.qty}))
      const filterProductos = productsData.filter(p => !listOption.productsInMovement.some(e => p.id === e.id));
      listOption.products = [...filterProductos]
    })
  }
}

const loadStockPositions = () => {
  StockPositionService.listBasic().then(res => {
    listOption.stockPositions = [...res.data]
  })
}

const restartMovementDetailModel = () => {
  Object.assign(movementDetail, new MovementDetail())
  Object.assign(currentMovement, new Movement())
}

const buildStaticFilter = (movement: IMovement): IFilter => {
  staticFilters.value = {
    movementId: {
      operator: FilterOperatorOptions.AND,
      constraints: [{value: movement.id, matchMode: FilterMatchMode.EQUALS}],
    },
  }
  return staticFilters.value
}

const onItemsLoaded = (info): void => {
  listOption.productsInMovement = [...info.map((det) => det.product)]
}

const onDetailRowEditInit = (info): void => {
  Object.assign(currentMovement, info.newData.movement)
  loadStockPositions()
  restartMovementDetailModel()
  Object.assign(movementDetail, info.newData)
}

const onDetailRowAddInit = (movement): void => {
  Object.assign(currentMovement, movement)
  loadStockPositions()
  Object.assign(movementDetail, new MovementDetail())
  movementDetail.movement = {}
  Object.assign(movementDetail.movement, {...movement})
}

const onDetailRowEditSave = (): void => {
  if (validator.value.movementDetail.$invalid) {
    customToast.errorToast(t('error.invalidForm'))
    return
  }
  editingStatusDetail.value = EditingStatus.SAVING
  if (movementDetail.id) {
    MovementDetailService.update(toRaw(movementDetail), movementDetail.id)
      .then(() => {
        restartMovementDetailModel()
        editingStatusDetail.value = EditingStatus.SAVED
        customToast.successToast(t('movement.actions.update.success'))
      })
      .catch(() => {
        editingStatusDetail.value = EditingStatus.ERROR
        customToast.errorToast(t('movement.actions.update.error'))
      })
  } else {
    MovementDetailService.save(toRaw(movementDetail))
      .then(() => {
        restartMovementDetailModel()
        editingStatusDetail.value = EditingStatus.SAVED
        customToast.successToast(t('movement.actions.save.success'))
      })
      .catch(() => {
        editingStatusDetail.value = EditingStatus.ERROR
        customToast.errorToast(t('movement.actions.save.error'))
      })
  }
}

const onDetailRowEditCancel = (): void => {
  restartMovementDetailModel()
}

const confirmMovement = async (row: any): Promise<void> => {
  const question = TranslationService.getInstanced().t('movement.actions.confirm.question', {movement: row.no})
  confirm.openGenericConfirm('general', 'movement.actions.confirm.title',
    question, () => acceptConfirm(row.id))
}

/**
 * Confirm item.
 */
const acceptConfirm = (id: number) => {
  editingStatusDetail.value = EditingStatus.SAVING
  movementService.confirm(id).then(() => {
    editingStatusMovement.value = EditingStatus.SAVED
    customToast.successToast('movement.actions.confirm.success')
  }).catch(() => {
    editingStatusMovement.value = EditingStatus.ERROR
    customToast.responseErrorToast('movement.actions.confirm.error')
  })
}
</script>
