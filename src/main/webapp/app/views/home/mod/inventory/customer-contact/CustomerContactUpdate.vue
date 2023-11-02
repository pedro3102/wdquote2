<template>
  <FormDialog
    @close="goBack"
    @accept="saveCatalog"
    :header="$t('entity.home.createOrEditLabel', !customerContact?.id ? 1 : 2, { named: { entity: $t('customerContact.detail.title') } })"
    :validate="validator.customerContact.$invalid"
  >
    <div class="formgrid grid pt-2">
      <div class="field col-12 md:col-6">
        <FormGroupInput
          id="field_code"
          v-model="customerContact.code"
          name="field_code"
          :label="$t('customerContact.code')"
          :rules="validator.customerContact.code"
        >
        </FormGroupInput>
      </div>
      <div class="field col-12 md:col-6">
        <FormGroupInput
          id="field_name"
          v-model="customerContact.name"
          name="field_code"
          :label="$t('customerContact.name')"
          :rules="validator.customerContact.name"
        >
        </FormGroupInput>
      </div>
      <div class="field col-12 md:col-6">
        <FormGroupInput id="field_phone" v-model="customerContact.phone" name="field_phone" :label="$t('customerContact.phone')">
        </FormGroupInput>
      </div>
      <div class="field col-12 md:col-6">
        <FormGroupInput
          id="field_email"
          v-model="customerContact.email"
          name="field_email"
          :label="$t('customerContact.email')"
          :rules="validator.customerContact.email"
        >
        </FormGroupInput>
      </div>
      <div class="field col-12 md:col-6">
        <FormGroupDropdown
          id="field_customer"
          v-model="customerContact.customer"
          name="field_customer"
          :label="$t('customerContact.customer')"
          :options="customers"
          optionLabel="name"
          :rules="validator.customerContact.customer"
        >
        </FormGroupDropdown>
      </div>
      <div class="field col-12 md:col-6">
        <FormGroupInput
          id="field_salespersonCode"
          v-model="customerContact.salespersonCode"
          name="field_salespersonCode"
          :label="$t('customerContact.salespersonCode')"
        >
          :rules="validator.customerContact.salespersonCode"
        </FormGroupInput>
      </div>
      <div class="field col-12 md:col-6">
        <FormGroupDropdown
          id="field_language"
          v-model="customerContact.language"
          name="field_language"
          :label="$t('customerContact.language')"
          :options="languages"
          optionLabel="name"
          :rules="validator.customerContact.language"
        >
        </FormGroupDropdown>
      </div>
      <div class="field col-12 md:col-6">
        <FormGroupInputSwitch
          id="field_isDefaultContact"
          v-model="customerContact.isDefaultContact"
          name="field_isDefaultContact"
          :label="$t('customerContact.isDefaultContact')"
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
import FormGroupInput from '@/shared/components/ui/form/FormGroupInput.vue'
import FormGroupDropdown from '@/shared/components/ui/form/FormGroupDropdown.vue'
import FormGroupNumber from '@/shared/components/ui/form/FormGroupNumber.vue'
import FormGroupInputSwitch from '@/shared/components/ui/form/FormGroupInputSwitch.vue'
import CustomerContactService from '@/views/home/mod/inventory/customer-contact/customer-contact-service'
import { ICustomerContact, CustomerContact } from '@/views/home/mod/inventory/customer-contact/customer-contact-model'
import customerContactRules from '@/views/home/mod/inventory/customer-contact/customer-contact-rules'
import { useCustomToast } from '@/shared/composables/custom-toast'
import { useI18n } from 'vue-i18n'
import CustomerService from '@/views/home/mod/inventory/customer/customer-service'
import { ICustomer } from '@/views/home/mod/inventory/customer/customer-model'
import { ILanguage } from '@/views/home/mod/inventory/language/language-model'
import LanguageService from '@/views/home/mod/inventory/language/language-service'

export default defineComponent({
  name: 'CustomerContactUpdate',
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
        vm.loadCustomerContact(String(to.params.id))
      }
    })
  },
  setup: () => {
    const route = useRoute()
    const { t } = useI18n()
    const router = useRouter()
    const customerContact: ICustomerContact = reactive(new CustomerContact())
    const validator = useVuelidate(customerContactRules, { customerContact })
    const customToast = useCustomToast()
    const customers: ICustomer[] = reactive([])
    const languages: ILanguage[] = reactive([])

    const loadCustomerContact = async (id: number): Promise<void> => {
      const { data } = await CustomerContactService.find(id)
      if (data) {
        Object.assign(customerContact, data)
      }
    }

    const goBack = (): void => {
      router.back()
    }

    CustomerService.listBasic().then(res => {
      Object.assign(customers, res.data)
    })

    LanguageService.list(LanguageService.baseUrl).then(res => {
      Object.assign(languages, res.data)
    })

    const saveCatalog = (): void => {
      if (route.params.id && customerContact.id) {
        CustomerContactService.update(toRaw(customerContact), customerContact.id)
          .then(() => {
            customToast.successToast(t('customerContact.actions.update.success'))
            goBack()
          })
          .catch(() => {
            customToast.errorToast(t('customerContact.actions.update.error'))
            goBack()
          })
      } else {
        CustomerContactService.save(toRaw(customerContact))
          .then(() => {
            customToast.successToast(t('customerContact.actions.save.success'))
            goBack()
          })
          .catch(() => {
            customToast.errorToast(t('customerContact.actions.save.error'))
            goBack()
          })
      }
    }

    return {
      customerContact,
      validator,
      customers,
      languages,
      goBack,
      saveCatalog,
      loadCustomerContact,
    }
  },
})
</script>
