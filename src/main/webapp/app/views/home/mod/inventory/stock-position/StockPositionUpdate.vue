<template>
  <FormDialog
    :header="$t('entity.home.createOrEditLabel', !stockPosition?.id ? 1 : 2, { named: { entity: $t('stockPosition.detail.title') } })"
    :validate="validator.stockPosition.$invalid"
    @accept="saveCatalog"
    @close="goBack"
  >
    <div class="formgrid grid pt-2">
      <div class="field col-12 md:col-6">
        <FormGroupInput
          id="field_name"
          v-model="stockPosition.name"
          :label="$t('stockPosition.name')"
          :rules="validator.stockPosition.name"
          name="field_name"
        >
        </FormGroupInput>
      </div>
      <div class="field col-12 md:col-6">
        <FormGroupInput
          id="field_description"
          v-model="stockPosition.description"
          :label="$t('stockPosition.description')"
          :rules="validator.stockPosition.description"
          name="field_description"
        >
        </FormGroupInput>
      </div>
    </div>
  </FormDialog>
</template>

<script lang="ts">
import {defineComponent, reactive, toRaw} from 'vue'
import {useVuelidate} from '@vuelidate/core'
import {RouteLocationNormalized, useRoute, useRouter} from 'vue-router'
import FormDialog from '@/shared/components/ui/form/FormDialog.vue'
import FormGroupInput from '@/shared/components/ui/form/FormGroupInput.vue'
import FormGroupDropdown from '@/shared/components/ui/form/FormGroupDropdown.vue'
import StockPositionService from '@/views/home/mod/inventory/stock-position/stock-position-service'
import {IStockPosition, StockPosition} from '@/views/home/mod/inventory/stock-position/stock-position-model'
import stockPositionRules from '@/views/home/mod/inventory/stock-position/stock-position-rules'
import {useCustomToast} from '@/shared/composables/custom-toast'
import {useI18n} from 'vue-i18n'

export default defineComponent({
  name: 'StockPositionUpdate',
  components: {
    FormDialog,
    FormGroupInput,
    FormGroupDropdown,
  },
  beforeRouteEnter(to: RouteLocationNormalized, from: RouteLocationNormalized, next) {
    next((vm: any) => {
      if (to.params.id) {
        vm.loadStockPosition(String(to.params.id))
      }
    })
  },
  setup: () => {
    const route = useRoute()
    const {t} = useI18n()
    const router = useRouter()
    const stockPosition: IStockPosition = reactive(new StockPosition())
    const validator = useVuelidate(stockPositionRules, {stockPosition})
    const customToast = useCustomToast()

    const loadStockPosition = async (id: number): Promise<void> => {
      const {data} = await StockPositionService.find(id)
      if (data) {
        Object.assign(stockPosition, data)
      }
    }

    const goBack = (): void => {
      router.back()
    }

    const saveCatalog = (): void => {
      if (route.params.id && stockPosition.id) {
        StockPositionService.update(toRaw(stockPosition), stockPosition.id)
          .then(() => {
            customToast.successToast(t('stockPosition.actions.update.success'))
            goBack()
          })
          .catch(() => {
            customToast.errorToast(t('stockPosition.actions.update.error'))
            goBack()
          })
      } else {
        StockPositionService.save(toRaw(stockPosition))
          .then(() => {
            customToast.successToast(t('stockPosition.actions.save.success'))
            goBack()
          })
          .catch(() => {
            customToast.errorToast(t('stockPosition.actions.save.error'))
            goBack()
          })
      }
    }

    return {
      stockPosition,
      validator,
      goBack,
      saveCatalog,
      loadStockPosition,
    }
  },
})
</script>
