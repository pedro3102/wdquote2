<template>
  <FormDialog
    @close="goBack"
    @accept="saveCatalog"
    :header="$t('entity.home.createOrEditLabel', !taxAreaCode?.id ? 1 : 2, { named: { entity: $t('taxAreaCode.detail.title') } })"
    :validate="validator.taxAreaCode.$invalid"
  >
    <div class="formgrid grid pt-2">
      <div class="field col-12 md:col-6">
        <FormGroupInput
          id="field_code"
          v-model="taxAreaCode.code"
          name="field_code"
          :label="$t('taxAreaCode.code')"
          :rules="validator.taxAreaCode.code"
        >
        </FormGroupInput>
      </div>
      <div class="field col-12 md:col-6">
        <FormGroupInput
          id="field_name"
          v-model="taxAreaCode.name"
          name="field_name"
          :label="$t('taxAreaCode.name')"
          :rules="validator.taxAreaCode.name"
        >
        </FormGroupInput>
      </div>
    </div>
  </FormDialog>
</template>

<script lang="ts">
import { defineComponent, reactive, toRaw } from 'vue'
import { useVuelidate } from '@vuelidate/core'
import { RouteLocationNormalized, useRoute, useRouter } from 'vue-router'
import FormDialog from '@/shared/components/ui/form/FormDialog.vue'
import FormGroupInput from '@/shared/components/ui/form/FormGroupInput.vue'
import TaxAreaCodeService from '@/views/home/mod/inventory/tax-area-code/tax-area-code-service'
import { TaxAreaCode, ITaxAreaCode } from '@/views/home/mod/inventory/tax-area-code/tax-area-code-model'
import taxAreaCodeRules from '@/views/home/mod/inventory/tax-area-code/tax-area-code-rules'
import { useCustomToast } from '@/shared/composables/custom-toast'
import { useI18n } from 'vue-i18n'

export default defineComponent({
  name: 'TaxAreaCodeUpdate',
  components: {
    FormDialog,
    FormGroupInput,
  },
  beforeRouteEnter(to: RouteLocationNormalized, from: RouteLocationNormalized, next) {
    next((vm: any) => {
      if (to.params.id) {
        vm.loadTaxAreaCode(String(to.params.id))
      }
    })
  },
  setup: () => {
    const route = useRoute()
    const { t } = useI18n()
    const router = useRouter()
    const taxAreaCode: ITaxAreaCode = reactive(new TaxAreaCode())
    const validator = useVuelidate(taxAreaCodeRules, { taxAreaCode })
    const customToast = useCustomToast()

    const loadTaxAreaCode = async (login: string): Promise<void> => {
      const { data } = await TaxAreaCodeService.find(login)
      if (data) {
        Object.assign(taxAreaCode, data)
      }
    }

    const goBack = (): void => {
      router.back()
    }

    const saveCatalog = (): void => {
      if (route.params.id && taxAreaCode.id) {
        TaxAreaCodeService.update(toRaw(taxAreaCode), taxAreaCode.id)
          .then(() => {
            customToast.successToast(t('taxAreaCode.actions.update.success'))
            goBack()
          })
          .catch(() => {
            customToast.errorToast(t('taxAreaCode.actions.update.error'))
            goBack()
          })
      } else {
        TaxAreaCodeService.save(toRaw(taxAreaCode))
          .then(() => {
            customToast.successToast(t('taxAreaCode.actions.save.success'))
            goBack()
          })
          .catch(() => {
            customToast.errorToast(t('taxAreaCode.actions.save.error'))
            goBack()
          })
      }
    }

    return {
      taxAreaCode,
      validator,
      goBack,
      saveCatalog,
      loadTaxAreaCode,
    }
  },
})
</script>
