<template>
  <FormDialog
    @close="goBack"
    @accept="saveCatalog"
    :header="$t('entity.home.createOrEditLabel', !customer?.id ? 1 : 2, { named: { entity: $t('customer.detail.title') } })"
    :validate="validator.customer.$invalid"
  >
    <div class="formgrid grid pt-2">
      <div class="field col-12 md:col-6">
        <FormGroupInput
          id="field_code"
          v-model="customer.code"
          name="field_code"
          :label="$t('customer.code')"
          :rules="validator.customer.code"
        >
        </FormGroupInput>
      </div>
      <div class="field col-12 md:col-6">
        <FormGroupInput
          id="field_name"
          v-model="customer.name"
          name="field_name"
          :label="$t('customer.name')"
          :rules="validator.customer.name"
        >
        </FormGroupInput>
      </div>
      <div class="field col-12 md:col-6">
        <FormGroupDropdown
          id="field_deliveryZone"
          v-model="customer.deliveryZone"
          name="field_deliveryZone"
          :label="$t('customer.deliveryZone')"
          :options="deliveryZones"
          optionLabel="name"
          :rules="validator.customer.deliveryZone"
        >
        </FormGroupDropdown>
      </div>
      <div class="field col-12 md:col-6">
        <FormGroupInput id="field_address" v-model="customer.address" name="field_address" :label="$t('customer.address')">
        </FormGroupInput>
      </div>
      <div class="field col-12 md:col-6">
        <FormGroupDropdown
          id="field_taxAreaCode"
          v-model="customer.taxAreaCode"
          name="field_taxAreaCode"
          :label="$t('customer.taxAreaCode')"
          :options="taxAreaCodes"
          optionLabel="name"
          :rules="validator.customer.taxAreaCode"
        >
        </FormGroupDropdown>
      </div>
      <div class="field col-12 md:col-6">
        <FormGroupInput
          id="field_taxExemptionCode"
          v-model="customer.taxExemptionCode"
          name="field_taxExemptionCode"
          :label="$t('customer.taxExemptionCode')"
        >
        </FormGroupInput>
      </div>
      <div class="field col-12 md:col-6">
        <FormGroupInput
          id="field_paymentTerms"
          v-model="customer.paymentTerms"
          name="field_paymentTerms"
          :label="$t('customer.paymentTerms')"
        >
        </FormGroupInput>
      </div>
      <div class="field col-12 md:col-6">
        <FormGroupNumber
          id="field_creditLimit"
          v-model="customer.creditLimit"
          name="field_creditLimit"
          :maxFractionDigits="2"
          :label="$t('customer.creditLimit')"
        >
        </FormGroupNumber>
      </div>
      <div class="field col-12 md:col-6">
        <FormGroupInputSwitch id="field_taxLiable" v-model="customer.taxLiable" name="field_taxLiable" :label="$t('customer.taxLiable')">
        </FormGroupInputSwitch>
      </div>
      <div class="field col-12 md:col-6">
        <FormGroupInputSwitch id="field_blocked" v-model="customer.blocked" name="field_blocked" :label="$t('customer.blocked')">
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
import FormGroupInput from '@/shared/components/ui/form/FormGroupInput.vue'
import FormGroupDropdown from '@/shared/components/ui/form/FormGroupDropdown.vue'
import FormGroupNumber from '@/shared/components/ui/form/FormGroupNumber.vue'
import FormGroupInputSwitch from '@/shared/components/ui/form/FormGroupInputSwitch.vue'
import CustomerService from '@/views/home/mod/inventory/customer/customer-service'
import { ICustomer, Customer } from '@/views/home/mod/inventory/customer/customer-model'
import customerRules from '@/views/home/mod/inventory/customer/customer-rules'
import { useCustomToast } from '@/shared/composables/custom-toast'
import { useI18n } from 'vue-i18n'
import { IDeliveryZone } from '@/views/home/mod/inventory/delivery-zone/delivery-zone-model'
import DeliveryZoneService from '@/views/home/mod/inventory/delivery-zone/delivery-zone-service'
import { ITaxAreaCode } from '@/views/home/mod/inventory/tax-area-code/tax-area-code-model'
import TaxAreaCodeService from '@/views/home/mod/inventory/tax-area-code/tax-area-code-service'

export default defineComponent({
  name: 'CustomerUpdate',
  components: {
    FormDialog,
    FormGroupInput,
    FormGroupDropdown,
    FormGroupNumber,
    FormGroupInputSwitch,
  },
  beforeRouteEnter(to: RouteLocationNormalized, from: RouteLocationNormalized, next) {
    next((vm: any) => {
      if (to.params.id) {
        vm.loadCustomer(String(to.params.id))
      }
    })
  },
  setup: () => {
    const route = useRoute()
    const { t } = useI18n()
    const router = useRouter()
    const customer: ICustomer = reactive(new Customer())
    const validator = useVuelidate(customerRules, { customer })
    const customToast = useCustomToast()
    const deliveryZones: IDeliveryZone[] = reactive([])
    const taxAreaCodes: ITaxAreaCode[] = reactive([])

    const loadCustomer = async (id: number): Promise<void> => {
      const { data } = await CustomerService.find(id)
      if (data) {
        Object.assign(customer, data)
      }
    }

    const goBack = (): void => {
      router.back()
    }

    DeliveryZoneService.list(DeliveryZoneService.baseUrl).then(res => {
      Object.assign(deliveryZones, res.data)
    })

    TaxAreaCodeService.list(TaxAreaCodeService.baseUrl).then(res => {
      Object.assign(taxAreaCodes, res.data)
    })

    const saveCatalog = (): void => {
      if (route.params.id && customer.id) {
        CustomerService.update(toRaw(customer), customer.id)
          .then(() => {
            customToast.successToast(t('customer.actions.update.success'))
            goBack()
          })
          .catch(() => {
            customToast.errorToast(t('customer.actions.update.error'))
            goBack()
          })
      } else {
        CustomerService.save(toRaw(customer))
          .then(() => {
            customToast.successToast(t('customer.actions.save.success'))
            goBack()
          })
          .catch(() => {
            customToast.errorToast(t('customer.actions.save.error'))
            goBack()
          })
      }
    }

    return {
      customer,
      validator,
      deliveryZones,
      taxAreaCodes,
      goBack,
      saveCatalog,
      loadCustomer,
    }
  },
})
</script>
