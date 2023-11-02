<template>
  <FormDialog
    @close="goBack"
    @accept="saveCatalog"
    :header="
      $t('unitOfMeasureConversion.home.createLabel', !unitOfMeasureConversion?.id ? 1 : 2, {
        named: { entity: $t('unitOfMeasureConversion.detail.title') },
      })
    "
    :validate="validator.unitOfMeasureConversion.$invalid"
  >
    <div class="formgrid grid pt-2">
      <div class="field col-12 md:col-6">
        <FormGroupDropdown
          id="field_unitOfMeasure"
          v-model="unitOfMeasureConversion.uom"
          name="field_unitOfMeasure"
          :disabled="true"
          :label="$t('unitOfMeasure.detail.title')"
          :options="[unitOfMeasure]"
          optionLabel="name"
          :rules="validator.unitOfMeasureConversion.uom"
        >
        </FormGroupDropdown>
      </div>
      <div class="field col-12 md:col-6">
        <FormGroupNumber
          id="field_conversionFactor"
          v-model="unitOfMeasureConversion.conversionFactor"
          name="field_conversionFactor"
          :maxFractionDigits="2"
          :label="$t('unitOfMeasureConversion.conversionFactor')"
          :rules="validator.unitOfMeasureConversion.conversionFactor"
        >
        </FormGroupNumber>
      </div>
      <div class="field col-12 md:col-6">
        <FormGroupDropdown
          id="field_uomEquivalent"
          v-model="unitOfMeasureConversion.uomEquivalent"
          name="field_unitOfMeasure"
          :label="$t('unitOfMeasureConversion.uomEquivalent')"
          :options="uoms"
          optionLabel="name"
          :rules="validator.unitOfMeasureConversion.uomEquivalent"
        >
        </FormGroupDropdown>
      </div>
    </div>
  </FormDialog>
</template>

<script lang="ts">
import { defineComponent, reactive, toRaw } from 'vue'
import { useVuelidate } from '@vuelidate/core'
import { RouteLocationNormalized, useRoute, useRouter } from 'vue-router'
import FormDialog from '@/shared/components/ui/form/FormDialog.vue'
import FormGroupDropdown from '@/shared/components/ui/form/FormGroupDropdown.vue'
import FormGroupNumber from '@/shared/components/ui/form/FormGroupNumber.vue'
import UnitOfMeasureService from '@/views/home/mod/inventory/unit-of-measure/unit-of-measure-service'
import { IUnitOfMeasure, UnitOfMeasure } from '@/views/home/mod/inventory/unit-of-measure/unit-of-measure-model'
import unitOfMeasureConversionRules from '@/views/home/mod/inventory/unit-of-measure-conversion/unit-of-measure-conversion-rules'
import { useCustomToast } from '@/shared/composables/custom-toast'
import { useI18n } from 'vue-i18n'
import {
  IUnitOfMeasureConversion,
  UnitOfMeasureConversion,
} from '@/views/home/mod/inventory/unit-of-measure-conversion/unit-of-measure-conversion-model'
import UnitOfMeasureConversionService from '@/views/home/mod/inventory/unit-of-measure-conversion/unit-of-measure-conversion-service'

export default defineComponent({
  name: 'UnitOfMeasureConversion',
  components: {
    FormDialog,
    FormGroupDropdown,
    FormGroupNumber,
  },
  beforeRouteEnter(to: RouteLocationNormalized, from: RouteLocationNormalized, next) {
    next((vm: any) => {
      if (to.params.id) {
        if (to.params.convId) {
          vm.loadUnitOfMeasureConversion(Number(to.params.convId))
        } else {
          vm.loadUnitOfMeasure(String(to.params.id))
        }
      }
    })
  },
  emits: ['conversion-updated'],
  setup: (props, { emit }) => {
    const route = useRoute()
    const { t } = useI18n()
    const router = useRouter()
    const unitOfMeasureConversion: IUnitOfMeasureConversion = reactive(new UnitOfMeasureConversion())
    const unitOfMeasure: IUnitOfMeasure = reactive(new UnitOfMeasure())
    const validator = useVuelidate(unitOfMeasureConversionRules, { unitOfMeasureConversion })
    const customToast = useCustomToast()
    const uoms: IUnitOfMeasure[] = reactive([])

    const loadUnitOfMeasure = async (id: number): Promise<void> => {
      const { data } = await UnitOfMeasureService.find(id)
      if (data) {
        Object.assign(unitOfMeasure, data)
        if (unitOfMeasure) {
          unitOfMeasureConversion.uom = data
        }
      }
    }

    const loadUnitOfMeasureConversion = async (id: number): Promise<void> => {
      const { data } = await UnitOfMeasureConversionService.find(id)
      if (data) {
        Object.assign(unitOfMeasure, data.uom)
        Object.assign(unitOfMeasureConversion, data)
      }
    }

    const goBack = (): void => {
      router.back()
    }

    UnitOfMeasureService.list(UnitOfMeasureService.baseUrl).then(res => {
      Object.assign(uoms, res.data)
    })

    const saveCatalog = (): void => {
      if (route.params.convId && unitOfMeasureConversion.id) {
        UnitOfMeasureConversionService.update(toRaw(unitOfMeasureConversion), unitOfMeasureConversion.id)
          .then(() => {
            emit('conversion-updated', unitOfMeasure)
            customToast.successToast(t('unitOfMeasure.actions.update.success'))
            goBack()
          })
          .catch(() => {
            customToast.errorToast(t('unitOfMeasure.actions.update.error'))
            goBack()
          })
      } else {
        UnitOfMeasureConversionService.save(toRaw(unitOfMeasureConversion))
          .then(() => {
            emit('conversion-updated', unitOfMeasure)
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
      unitOfMeasureConversion,
      unitOfMeasure,
      validator,
      uoms,
      goBack,
      saveCatalog,
      loadUnitOfMeasure,
      loadUnitOfMeasureConversion,
    }
  },
})
</script>
