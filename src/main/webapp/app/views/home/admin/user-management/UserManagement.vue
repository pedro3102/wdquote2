<template>
  <!--  We add this for /new /:id/edit /:id/delete child routes-->
  <router-view />
  <DataTableList
    :entity="userManageEntity"
    :service="UserManagementService"
    :addInRowObject="user"
    v-model:editingStatus="editingStatus"
    @row-edit-init="onRowEditInit"
    @row-add-init="onRowAddInit"
    @row-edit-save="onRowEditSave"
    @row-edit-cancel="onRowEditCancel"
  >
    <template #editRowInPlace="{ data, field }">
      <FormGroupInput
        v-if="field === 'login'"
        id="field_login"
        v-model="user.login"
        name="field_login"
        :rules="validator.user.login"
        :uppercase="false"
        :label="$t('user.login')"
        :customErrorText="validator.user?.login?.pattern?.$invalid ? $t('entity.validation.patternLogin') : ''"
      />
      <FormGroupMultiSelect
        v-if="field === 'authorities'"
        v-model="user.authorities"
        :options="authorities"
        :placeholder="$t('user.authorities')"
        class="p-column-filter"
        optionLabel="name"
        :rules="validator.user.authorities"
        :maxSelectedLabels="2"
      >
        <template #value="slotProps">
          <span v-for="(child, index) in slotProps.data.value" :key="index" v-html="renderFilterSelectOption(child)" />
        </template>
        <template #option="slotProps">
          <span v-html="renderFilterSelectOption(slotProps.data.option)" />
        </template>
      </FormGroupMultiSelect>
      <FormGroupInput
        v-if="field === 'email'"
        :id="`field_email_${data['id']}`"
        v-model="user.email"
        :name="`field_email_${data['id']}`"
        :rules="validator.user.email"
        :label="$t('user.email')"
      />
      <FormGroupInput
        v-if="field === 'firstName'"
        id="field_firstName"
        v-model="user.firstName"
        name="field_firstName"
        :label="$t('user.firstName')"
        :rules="validator.user.firstName"
      />
      <FormGroupInput
        v-if="field === 'lastName'"
        id="field_lastName"
        v-model="user.lastName"
        name="field_lastName"
        :label="$t('user.lastName')"
        :rules="validator.user.lastName"
      />
    </template>
  </DataTableList>
</template>

<script lang="ts" setup>
import DataTableList from '@/shared/components/ui/datatable/DataTableList.vue'
import { userManageEntity } from './user-management-entity'
import UserManagementService from './user-management-service'
import customRender from '@/shared/renders/custom-render'
import { ROLES } from '@/shared/const/auth-constants'
import { EditingStatus, PrimeSeverities } from '@/shared/const/entity-constants'
import FormGroupMultiSelect from '@/shared/components/ui/form/FormGroupMultiSelect.vue'
import FormGroupInput from '@/shared/components/ui/form/FormGroupInput.vue'
import { IRole } from '@/views/home/admin/role/role-model'
import RoleService from '@/views/home/admin/role/role-service'
import { reactive, Ref, ref, toRaw } from 'vue'
import { useVuelidate } from '@vuelidate/core'
import userRules from '@/views/home/admin/user-management/user-management-rules'
import { IUser, User } from '@/views/home/admin/user-management/user.model'
import { useI18n } from 'vue-i18n'
import { useCustomToast } from '@/shared/composables/custom-toast'

const { t } = useI18n()
const customToast = useCustomToast()
const authorities: IRole[] = reactive([])
const user: IUser = reactive(new User())
const editingStatus: Ref<EditingStatus> = ref(EditingStatus.NONE)
const renderFilterSelectOption = ({ name }) =>
  customRender.renderPrimeTag(name, name === ROLES.ROLE_ADMIN ? PrimeSeverities.DANGER : PrimeSeverities.INFO)

const initAuthorities = async (): Promise<void> => {
  const { data } = await RoleService.list()
  if (data) {
    data.forEach(role => {
      authorities.push(role)
    })
  }
}

const restartConversionModel = () => {
  Object.assign(user, new User())
}

const validator = useVuelidate(userRules, { user })

const onRowEditSave = (): void => {
  if (validator.value.user.$invalid) {
    customToast.errorToast(t('error.invalidForm'))
    return
  }
  editingStatus.value = EditingStatus.SAVING
  const copyUser = { ...user }
  copyUser.authorities = user.authorities.map(auth => auth.name)
  if (copyUser.id) {
    UserManagementService.update(toRaw(copyUser))
      .then(() => {
        restartConversionModel()
        editingStatus.value = EditingStatus.SAVED
        customToast.successToast(t('user.actions.update.success'))
      })
      .catch(() => {
        editingStatus.value = EditingStatus.ERROR
        customToast.errorToast(t('user.actions.update.error'))
      })
  } else {
    UserManagementService.save(toRaw(copyUser))
      .then(() => {
        restartConversionModel()
        editingStatus.value = EditingStatus.SAVED
        customToast.successToast(t('user.actions.save.success'))
      })
      .catch(() => {
        editingStatus.value = EditingStatus.ERROR
        customToast.errorToast(t('user.actions.save.error'))
      })
  }
}

const onRowEditInit = (info): void => {
  if (authorities.length === 0) initAuthorities()
  Object.assign(user, info.newData)
}

const onRowAddInit = (): void => {
  if (authorities.length === 0) initAuthorities()
}

const onRowEditCancel = () => {
  restartConversionModel()
}
</script>
