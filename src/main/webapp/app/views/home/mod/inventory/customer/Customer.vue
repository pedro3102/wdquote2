<template>
  <!--  We add this for /new /:id/edit /:id/delete child routes-->
  <router-view />
  <DataTableList :entity="customerEntity" :service="CustomerService" :onBeforeRowLoad="onBeforeRowLoad">
    <template #expansionDT="slotProps">
      <DataTableList
        :entity="customerChildContactEntity"
        :service="CustomerContactService"
        :staticFilters="() => buildStaticFilter(slotProps.data)"
        :addInRowObject="customerContact"
        :treeFather="slotProps.data"
        v-model:editingStatus="editingStatus"
        @row-edit-init="onContactRowEditInit"
        @row-add-init="onContactRowAddInit"
        @row-edit-save="onContactRowEditSave"
        @row-edit-cancel="onContactRowEditCancel"
      >
        <template #editRowInPlace="{ field }">
          <FormGroupInput
            v-if="field === 'code'"
            id="field_code"
            v-model="customerContact.code"
            name="field_code"
            :label="$t('customerContact.code')"
            :rules="validator.customerContact.code"
          >
          </FormGroupInput>
          <FormGroupInput
            v-if="field === 'name'"
            id="field_name"
            v-model="customerContact.name"
            name="field_code"
            :label="$t('customerContact.name')"
            :rules="validator.customerContact.name"
          >
          </FormGroupInput>
          <FormGroupInput
            v-if="field === 'phone'"
            id="field_phone"
            v-model="customerContact.phone"
            name="field_phone"
            :label="$t('customerContact.phone')"
          >
          </FormGroupInput>
          <FormGroupInput
            v-if="field === 'email'"
            id="field_email"
            v-model="customerContact.email"
            name="field_email"
            :label="$t('customerContact.email')"
            :rules="validator.customerContact.email"
          >
          </FormGroupInput>
          <FormGroupInput
            v-if="field === 'salespersonCode'"
            id="field_salespersonCode"
            v-model="customerContact.salespersonCode"
            name="field_salespersonCode"
            :label="$t('customerContact.salespersonCode')"
          >
            :rules="validator.customerContact.salespersonCode"
          </FormGroupInput>
          <FormGroupDropdown
            v-if="field === 'language'"
            id="field_language"
            v-model="customerContact.language"
            name="field_language"
            :label="$t('customerContact.language')"
            :options="languages"
            optionLabel="name"
            :rules="validator.customerContact.language"
          >
          </FormGroupDropdown>
          <FormGroupInputSwitch
            v-if="field === 'isDefaultContact'"
            id="field_isDefaultContact"
            v-model="customerContact.isDefaultContact"
            name="field_isDefaultContact"
            :label="$t('customerContact.isDefaultContact')"
          >
          </FormGroupInputSwitch>
        </template>
      </DataTableList>
    </template>
  </DataTableList>
</template>

<script lang="ts" setup>
import DataTableList from '@/shared/components/ui/datatable/DataTableList.vue'
import { customerEntity } from './customer-entity'
import { customerChildContactEntity } from '@/views/home/mod/inventory/customer-contact/customer-contact-entity'
import FormGroupInput from '@/shared/components/ui/form/FormGroupInput.vue'
import FormGroupDropdown from '@/shared/components/ui/form/FormGroupDropdown.vue'
import FormGroupInputSwitch from '@/shared/components/ui/form/FormGroupInputSwitch.vue'
import CustomerService from './customer-service'
import CustomerContactService from '@/views/home/mod/inventory/customer-contact/customer-contact-service'
import { IFilter } from '@/views/entities/common/entity-interface'
import { EditingStatus, FilterMatchMode, FilterOperatorOptions } from '@/shared/const/entity-constants'
import { Customer, ICustomer } from '@/views/home/mod/inventory/customer/customer-model'
import { reactive, ref, Ref, toRaw } from 'vue'
import { CustomerContact, ICustomerContact } from '@/views/home/mod/inventory/customer-contact/customer-contact-model'
import { useVuelidate } from '@vuelidate/core'
import customerContactRules from '@/views/home/mod/inventory/customer-contact/customer-contact-rules'
import { ILanguage } from '@/views/home/mod/inventory/language/language-model'
import LanguageService from '@/views/home/mod/inventory/language/language-service'
import { useCustomToast } from '@/shared/composables/custom-toast'
import { useI18n } from 'vue-i18n'

const { t } = useI18n()
const staticFilters = ref({})
const customerContact: ICustomerContact = reactive(new CustomerContact())
const customerUpdated: Ref<ICustomer> = ref({})
const validator = useVuelidate(customerContactRules, { customerContact })
const customToast = useCustomToast()
const editingStatus: Ref<EditingStatus> = ref(EditingStatus.NONE)
const languages: ILanguage[] = reactive([])

const loadLanguages = () => {
  LanguageService.list(LanguageService.baseUrl).then(res => {
    Object.assign(languages, res.data)
  })
}

const restartCustomerContactModel = () => {
  Object.assign(customerContact, new CustomerContact())
}

const buildStaticFilter = (customer: ICustomer): IFilter => {
  staticFilters.value = {
    customerId: {
      operator: FilterOperatorOptions.AND,
      constraints: [{ value: customer.id, matchMode: FilterMatchMode.EQUALS }],
    },
  }
  return staticFilters.value
}

const onBeforeRowLoad = (row: ICustomer): ICustomer => {
  const copy: ICustomer = row
  if (row.id === customerUpdated.value.id) {
    copy.defaultExpanded = true
  }
  return copy
}

const onContactRowEditInit = (info): void => {
  loadLanguages()
  restartCustomerContactModel()
  Object.assign(customerContact, info.newData)
}

const onContactRowAddInit = (customer): void => {
  loadLanguages()
  Object.assign(customerContact, new CustomerContact())
  customerContact.customer = {}
  console.log(customer)
  Object.assign(customerContact.customer, { ...customer })
  console.log(customerContact)
}

const onContactRowEditSave = (): void => {
  if (validator.value.customerContact.$invalid) {
    customToast.errorToast(t('error.invalidForm'))
    return
  }
  if (customerContact.id) {
    CustomerContactService.update(toRaw(customerContact), customerContact.id)
      .then(() => {
        restartCustomerContactModel()
        editingStatus.value = EditingStatus.SAVED
        customToast.successToast(t('customer.actions.update.success'))
      })
      .catch(() => {
        editingStatus.value = EditingStatus.ERROR
        customToast.errorToast(t('customer.actions.update.error'))
      })
  } else {
    CustomerContactService.save(toRaw(customerContact))
      .then(() => {
        restartCustomerContactModel()
        editingStatus.value = EditingStatus.SAVED
        customToast.successToast(t('customer.actions.save.success'))
      })
      .catch(() => {
        editingStatus.value = EditingStatus.ERROR
        customToast.errorToast(t('customer.actions.save.error'))
      })
  }
}

const onContactRowEditCancel = (): void => {
  restartCustomerContactModel()
}
</script>
