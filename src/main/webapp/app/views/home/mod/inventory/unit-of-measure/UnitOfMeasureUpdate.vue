<template>
  <FormDialog
    @close="goBack"
    @accept="saveCatalog"
    :header="$t('entity.home.createOrEditLabel', !unitOfMeasure?.id ? 1 : 2, { named: { entity: $t('unitOfMeasure.detail.title') } })"
    :validate="validator.unitOfMeasure.$invalid"
  >
    <div class="formgrid grid pt-2">
      <div class="field col-12 md:col-6">
        <FormGroupInput
          id="field_name"
          v-model="unitOfMeasure.name"
          name="field_name"
          :label="$t('unitOfMeasure.name')"
          :rules="validator.unitOfMeasure.name"
        >
        </FormGroupInput>
      </div>
      <div class="field col-12 md:col-6">
        <FormGroupInput
          id="field_abbreviation"
          v-model="unitOfMeasure.abbreviation"
          name="field_abbreviation"
          :label="$t('unitOfMeasure.abbreviation')"
          :rules="validator.unitOfMeasure.abbreviation"
        >
        </FormGroupInput>
      </div>
      <div class="field col-6 md:col-3">
        <FormGroupInputSwitch
          id="field_allowsDecimal"
          v-model="unitOfMeasure.allowsDecimal"
          name="field_allowsDecimal"
          :label="$t('unitOfMeasure.allowsDecimal')"
        >
        </FormGroupInputSwitch>
      </div>
    </div>
  </FormDialog>
</template>

<script lang="ts">
import { defineComponent, reactive, toRaw } from 'vue'
import { useVuelidate } from '@vuelidate/core'
import { RouteLocationNormalized, useRoute, useRouter } from 'vue-router'
import FormDialog from '@/shared/components/ui/form/FormDialog.vue'
import FormGroupInputSwitch from '@/shared/components/ui/form/FormGroupInputSwitch.vue'
import FormGroupInput from '@/shared/components/ui/form/FormGroupInput.vue'
import UnitOfMeasureService from '@/views/home/mod/inventory/unit-of-measure/unit-of-measure-service'
import { IUnitOfMeasure, UnitOfMeasure } from '@/views/home/mod/inventory/unit-of-measure/unit-of-measure-model'
import unitOfMeasureRules from '@/views/home/mod/inventory/unit-of-measure/unit-of-measure-rules'
import { useCustomToast } from '@/shared/composables/custom-toast'
import { useI18n } from 'vue-i18n'

export default defineComponent({
  name: 'UnitOfMeasureUpdate',
  components: {
    FormDialog,
    FormGroupInput,
    FormGroupInputSwitch,
  },
  beforeRouteEnter(to: RouteLocationNormalized, from: RouteLocationNormalized, next) {
    next((vm: any) => {
      if (to.params.id) {
        vm.loadUnitOfMeasure(String(to.params.id))
      }
    })
  },
  setup: () => {
    const route = useRoute()
    const { t } = useI18n()
    const router = useRouter()
    const unitOfMeasure: IUnitOfMeasure = reactive(new UnitOfMeasure())
    const validator = useVuelidate(unitOfMeasureRules, { unitOfMeasure })
    const customToast = useCustomToast()

    const loadUnitOfMeasure = async (id: number): Promise<void> => {
      const { data } = await UnitOfMeasureService.find(id)
      if (data) {
        Object.assign(unitOfMeasure, data)
      }
    }

    const goBack = (): void => {
      router.back()
    }

    const saveCatalog = (): void => {
      if (route.params.id && unitOfMeasure.id) {
        UnitOfMeasureService.update(toRaw(unitOfMeasure), unitOfMeasure.id)
          .then(() => {
            customToast.successToast(t('unitOfMeasure.actions.update.success'))
            goBack()
          })
          .catch(() => {
            customToast.errorToast(t('unitOfMeasure.actions.update.error'))
            goBack()
          })
      } else {
        UnitOfMeasureService.save(toRaw(unitOfMeasure))
          .then(() => {
            customToast.successToast(t('unitOfMeasure.actions.save.success'))
            goBack()
          })
          .catch(() => {
            customToast.errorToast(t('unitOfMeasure.actions.save.error'))
            goBack()
          })
      }
    }

    return {
      unitOfMeasure,
      validator,
      goBack,
      saveCatalog,
      loadUnitOfMeasure,
    }
  },
})
</script>
