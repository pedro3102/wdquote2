<template>
  <FormDialog
    :header="
      $t('movementDetail.home.createLabel', !movementDetail?.id ? 1 : 2, {
        named: { entity: $t('movementDetail.detail.title') },
      })
    "
    :validate="validator.movementDetail.$invalid"
    @accept="saveCatalog"
    @close="goBack"
  >
    <div class="formgrid grid pt-2">
      <div class="field col-12 md:col-6">
        <FormGroupDropdown
          id="field_product"
          v-model="movementDetail.product"
          :label="$t('movementDetail.product')"
          :options="products"
          :rules="validator.movementDetail.product"
          name="field_product"
          optionLabel="name">
        </FormGroupDropdown>
      </div>
      <div class="field col-12 md:col-6">
        <FormGroupInput
          id="field_vendorCode"
          v-model="movementDetail.vendorCode"
          :label="$t('movementDetail.vendorCode')"
          name="field_vendorCode">
        </FormGroupInput>
      </div>
      <div class="field col-12 md:col-6">
        <FormGroupNumber
          id="field_unitCost"
          v-model="movementDetail.unitCost"
          :label="$t('movementDetail.unitCost')"
          :maxFractionDigits="5"
          :rules="validator.movementDetail.unitCost"
          name="field_unitCost">
        </FormGroupNumber>
      </div>
      <div class="field col-12 md:col-6">
        <FormGroupNumber
          id="field_salePrice"
          v-model="movementDetail.salePrice"
          :label="$t('movementDetail.salePrice')"
          :maxFractionDigits="2"
          name="field_salePrice">
        </FormGroupNumber>
      </div>
      <div class="field col-12 md:col-6">
        <FormGroupNumber
          id="field_qty"
          v-model="movementDetail.qty"
          :label="$t('movementDetail.qty')"
          :maxFractionDigits="5"
          :rules="validator.movementDetail.qty"
          name="field_qty">
        </FormGroupNumber>
      </div>
    </div>
  </FormDialog>
</template>

<script lang="ts">
import {defineComponent, reactive, toRaw} from 'vue'
import {useVuelidate} from '@vuelidate/core'
import {RouteLocationNormalized, useRoute, useRouter} from 'vue-router'
import FormDialog from '@/shared/components/ui/form/FormDialog.vue'
import FormGroupDropdown from '@/shared/components/ui/form/FormGroupDropdown.vue'
import FormGroupNumber from '@/shared/components/ui/form/FormGroupNumber.vue'
import MovementService from '@/views/home/mod/inventory/movement/movement-service'
import {IMovement, Movement} from '@/views/home/mod/inventory/movement/movement-model'
import movementDetailRules from '@/views/home/mod/inventory/movement-detail/movement-detail-rules'
import {useCustomToast} from '@/shared/composables/custom-toast'
import {useI18n} from 'vue-i18n'
import {IMovementDetail, MovementDetail} from '@/views/home/mod/inventory/movement-detail/movement-detail-model'
import MovementDetailService from '@/views/home/mod/inventory/movement-detail/movement-detail-service'
import {IProduct} from "@/views/home/mod/inventory/product/product-model";
import ProductService from "@/views/home/mod/inventory/product/product-service";

export default defineComponent({
  name: 'MovementDetail',
  components: {
    FormDialog,
    FormGroupDropdown,
    FormGroupNumber,
  },
  beforeRouteEnter(to: RouteLocationNormalized, from: RouteLocationNormalized, next) {
    next((vm: any) => {
      if (to.params.id) {
        if (to.params.detId) {
          vm.loadMovementDetail(Number(to.params.detId))
        } else {
          vm.loadMovement(String(to.params.id))
        }
      }
    })
  },
  emits: ['movementDetail-updated'],
  setup: (props, {emit}) => {
    const route = useRoute()
    const {t} = useI18n()
    const router = useRouter()
    const movementDetail: IMovementDetail = reactive(new MovementDetail())
    const movement: IMovement = reactive(new Movement())
    const validator = useVuelidate(movementDetailRules, {movementDetail})
    const customToast = useCustomToast()
    const products: IProduct[] = reactive([])

    const loadMovement = async (id: number): Promise<void> => {
      const {data} = await MovementService.find(id)
      if (data) {
        Object.assign(movement, data)
        if (movement) {
          movementDetail.movement = data
        }
      }
    }

    const loadMovementDetail = async (id: number): Promise<void> => {
      const {data} = await MovementDetailService.find(id)
      if (data) {
        Object.assign(movement, data.movement)
        Object.assign(movementDetail, data)
      }
    }

    const goBack = (): void => {
      router.back()
    }

    ProductService.list(ProductService.baseUrl).then(res => {
      Object.assign(products, res.data)
    })

    const saveCatalog = (): void => {
      if (route.params.detId && movementDetail.id) {
        MovementDetailService.update(toRaw(movementDetail), movementDetail.id)
          .then(() => {
            emit('movementDetail-updated', movement)
            customToast.successToast(t('movement.actions.update.success'))
            goBack()
          })
          .catch(() => {
            customToast.errorToast(t('movement.actions.update.error'))
            goBack()
          })
      } else {
        MovementDetailService.save(toRaw(movementDetail))
          .then(() => {
            emit('movementDetail-updated', movement)
            customToast.successToast(t('movement.actions.save.success'))
            goBack()
          })
          .catch(() => {
            customToast.errorToast(t('movement.actions.save.error'))
            goBack()
          })
      }
    }

    return {
      movementDetail,
      movement,
      validator,
      products,
      goBack,
      saveCatalog,
      loadMovement,
      loadMovementDetail,
    }
  },
})
</script>
