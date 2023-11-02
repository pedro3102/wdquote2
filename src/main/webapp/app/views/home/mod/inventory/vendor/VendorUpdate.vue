<template>
  <FormDialog
    v-model:editingStatus="editingStatus"
    @close="goBack"
    @accept="saveCatalog"
    :header="$t('entity.home.createOrEditLabel', !vendor?.id ? 1 : 2, { named: { entity: $t('vendor.detail.title') } })"
    :validate="validator.vendor.$invalid"
  >
    <div class="formgrid grid pt-2">
      <div class="field col-12 md:col-6">
        <FormGroupInput id="field_code" v-model="vendor.code" name="field_code" :label="$t('vendor.code')" :rules="validator.vendor.code">
        </FormGroupInput>
      </div>
      <div class="field col-12 md:col-6">
        <FormGroupInput id="field_name" v-model="vendor.name" name="field_name" :label="$t('vendor.name')" :rules="validator.vendor.name">
        </FormGroupInput>
      </div>
      <div class="field col-12">
        <FormGroupInput
          id="field_address"
          v-model="vendor.address"
          name="field_address"
          :label="$t('vendor.address')"
          :rules="validator.vendor.address"
        >
        </FormGroupInput>
      </div>
    </div>
  </FormDialog>
</template>

<script lang="ts">
import { defineComponent, reactive, ref, Ref, toRaw } from 'vue'
import { useVuelidate } from '@vuelidate/core'
import { RouteLocationNormalized, useRoute, useRouter } from 'vue-router'
import FormDialog from '@/shared/components/ui/form/FormDialog.vue'
import FormGroupInput from '@/shared/components/ui/form/FormGroupInput.vue'
import VendorService from '@/views/home/mod/inventory/vendor/vendor-service'
import { IVendor, Vendor } from '@/views/home/mod/inventory/vendor/vendor-model'
import vendorRules from '@/views/home/mod/inventory/vendor/vendor-rules'
import { useLangStore } from '@/store/modules/i18n/translation-store'
import { useCustomToast } from '@/shared/composables/custom-toast'
import { useI18n } from 'vue-i18n'
import { EditingStatus } from '@/shared/const/entity-constants'
export default defineComponent({
  name: 'VendorUpdate',
  components: {
    FormDialog,
    FormGroupInput,
  },
  beforeRouteEnter(to: RouteLocationNormalized, from: RouteLocationNormalized, next) {
    next((vm: any) => {
      if (to.params.id) {
        vm.loadVendor(String(to.params.id))
      }
    })
  },
  setup: () => {
    const route = useRoute()
    const { t } = useI18n()
    const router = useRouter()
    const vendor: IVendor = reactive(new Vendor())
    const validator = useVuelidate(vendorRules, { vendor })
    const customToast = useCustomToast()
    const editingStatus: Ref<EditingStatus> = ref(EditingStatus.NONE)

    const loadVendor = async (login: string): Promise<void> => {
      const { data } = await VendorService.find(login)
      if (data) {
        Object.assign(vendor, data)
      }
    }

    const goBack = (): void => {
      router.back()
    }

    const saveCatalog = (): void => {
      if (route.params.id && vendor.id) {
        VendorService.update(toRaw(vendor), vendor.id)
          .then(() => {
            editingStatus.value = EditingStatus.SAVED
            customToast.successToast(t('vendor.actions.update.success'))
            goBack()
          })
          .catch(() => {
            editingStatus.value = EditingStatus.ERROR
            customToast.errorToast(t('vendor.actions.update.error'))
          })
      } else {
        VendorService.save(toRaw(vendor))
          .then(() => {
            editingStatus.value = EditingStatus.SAVED
            customToast.successToast(t('vendor.actions.save.success'))
            goBack()
          })
          .catch(() => {
            editingStatus.value = EditingStatus.ERROR
            customToast.errorToast(t('vendor.actions.save.error'))
          })
      }
    }

    return {
      vendor,
      validator,
      goBack,
      saveCatalog,
      loadVendor,
    }
  },
})
</script>
