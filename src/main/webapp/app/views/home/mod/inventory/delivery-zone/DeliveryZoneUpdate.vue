<template>
  <FormDialog
    @close="goBack"
    @accept="saveCatalog"
    :header="$t('entity.home.createOrEditLabel', !deliveryZone?.id ? 1 : 2, { named: { entity: $t('deliveryZone.detail.title') } })"
    :validate="validator.deliveryZone.$invalid"
  >
    <div class="formgrid grid pt-2">
      <div class="field col-12 md:col-6">
        <FormGroupInput
          id="field_code"
          v-model="deliveryZone.code"
          name="field_code"
          :label="$t('deliveryZone.code')"
          :rules="validator.deliveryZone.code"
        >
        </FormGroupInput>
      </div>
      <div class="field col-12 md:col-6">
        <FormGroupInput
          id="field_name"
          v-model="deliveryZone.name"
          name="field_name"
          :label="$t('deliveryZone.name')"
          :rules="validator.deliveryZone.name"
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
import DeliveryZoneService from '@/views/home/mod/inventory/delivery-zone/delivery-zone-service'
import { DeliveryZone, IDeliveryZone } from '@/views/home/mod/inventory/delivery-zone/delivery-zone-model'
import deliveryZoneRules from '@/views/home/mod/inventory/delivery-zone/delivery-zone-rules'
import { useCustomToast } from '@/shared/composables/custom-toast'
import { useI18n } from 'vue-i18n'

export default defineComponent({
  name: 'DeliveryZoneUpdate',
  components: {
    FormDialog,
    FormGroupInput,
  },
  beforeRouteEnter(to: RouteLocationNormalized, from: RouteLocationNormalized, next) {
    next((vm: any) => {
      if (to.params.id) {
        vm.loadDeliveryZone(String(to.params.id))
      }
    })
  },
  setup: () => {
    const route = useRoute()
    const { t } = useI18n()
    const router = useRouter()
    const deliveryZone: IDeliveryZone = reactive(new DeliveryZone())
    const validator = useVuelidate(deliveryZoneRules, { deliveryZone })
    const customToast = useCustomToast()

    const loadDeliveryZone = async (login: string): Promise<void> => {
      const { data } = await DeliveryZoneService.find(login)
      if (data) {
        Object.assign(deliveryZone, data)
      }
    }

    const goBack = (): void => {
      router.back()
    }

    const saveCatalog = (): void => {
      if (route.params.id && deliveryZone.id) {
        DeliveryZoneService.update(toRaw(deliveryZone), deliveryZone.id)
          .then(() => {
            customToast.successToast(t('deliveryZone.actions.update.success'))
            goBack()
          })
          .catch(() => {
            customToast.errorToast(t('deliveryZone.actions.update.error'))
            goBack()
          })
      } else {
        DeliveryZoneService.save(toRaw(deliveryZone))
          .then(() => {
            customToast.successToast(t('deliveryZone.actions.save.success'))
            goBack()
          })
          .catch(() => {
            customToast.errorToast(t('deliveryZone.actions.save.error'))
            goBack()
          })
      }
    }

    return {
      deliveryZone,
      validator,
      goBack,
      saveCatalog,
      loadDeliveryZone,
    }
  },
})
</script>
