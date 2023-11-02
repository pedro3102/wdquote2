<template>
  <FormDialog
    @close="goBack"
    @accept="saveCatalog"
    :header="$t('entity.home.createOrEditLabel', !user?.id ? 1 : 2, { named: { entity: $t('user.detail.title') } })"
    :validate="validator.user.$invalid"
  >
    <div class="formgrid grid pt-2">
      <div class="field col-12 md:col-6">
        <FormGroupInput id="field_login" v-model="user.login" name="field_login" :label="$t('user.login')" :rules="validator.user.login">
          <template #customErrors v-if="validator.user?.login?.pattern?.$invalid">
            <small class="invalid-feedback" v-text="$t('entity.validation.patternLogin')" />
          </template>
        </FormGroupInput>
      </div>
      <div class="field col-12 md:col-6">
        <FormGroupInput id="field_email" v-model="user.email" name="field_email" :label="$t('user.email')" :rules="validator.user.email">
        </FormGroupInput>
      </div>
      <div class="field col-12 md:col-6">
        <FormGroupInput
          id="field_firstName"
          v-model="user.firstName"
          name="field_firstName"
          :label="$t('user.firstName')"
          :rules="validator.user.firstName"
        >
        </FormGroupInput>
      </div>
      <div class="field col-12 md:col-6">
        <FormGroupInput id="field_lastName" v-model="user.lastName" name="field_lastName" :label="$t('user.lastName')"> </FormGroupInput>
      </div>
      <div class="field col-6 md:col-3">
        <FormGroupInputSwitch id="field_activated" v-model="user.activated" name="field_activated" :label="$t('user.activated')">
        </FormGroupInputSwitch>
      </div>
      <div class="field col-6 md:col-3">
        <FormGroupDropdown
          id="field_langKey"
          v-model="user.langKey"
          name="field_langKey"
          :label="$t('user.langKey')"
          :options="langStore.getLanguages"
          optionLabel="name"
          optionValue="key"
        >
        </FormGroupDropdown>
      </div>
      <div class="field col-12 md:col-6">
        <FormGroupMultiSelect
          id="field_authorities"
          v-model="user.authorities"
          name="field_authorities"
          :label="$t('user.authorities')"
          optionLabel="name"
          optionValue="name"
          :options="authorities"
        >
        </FormGroupMultiSelect>
      </div>
    </div>
  </FormDialog>
</template>

<script lang="ts">
import { defineComponent, reactive, ref, Ref, toRaw } from 'vue'
import { useVuelidate } from '@vuelidate/core'
import { RouteLocationNormalized, useRoute, useRouter } from 'vue-router'
import FormDialog from '@/shared/components/ui/form/FormDialog.vue'
import FormGroupInputSwitch from '@/shared/components/ui/form/FormGroupInputSwitch.vue'
import FormGroupInput from '@/shared/components/ui/form/FormGroupInput.vue'
import FormGroupDropdown from '@/shared/components/ui/form/FormGroupDropdown.vue'
import FormGroupMultiSelect from '@/shared/components/ui/form/FormGroupMultiSelect.vue'
import UserManagementService from '@/views/home/admin/user-management/user-management-service'
import { IUser, User } from '@/views/home/admin/user-management/user.model'
import userRules from '@/views/home/admin/user-management/user-management-rules'
import { useLangStore } from '@/store/modules/i18n/translation-store'
import { useCustomToast } from '@/shared/composables/custom-toast'
import { useI18n } from 'vue-i18n'
import RoleService from '@/views/home/admin/role/role-service'
import { IRole } from '@/views/home/admin/role/role-model'

export default defineComponent({
  name: 'UserManagement',
  components: {
    FormDialog,
    FormGroupInput,
    FormGroupInputSwitch,
    FormGroupDropdown,
    FormGroupMultiSelect,
  },
  beforeRouteEnter(to: RouteLocationNormalized, from: RouteLocationNormalized, next) {
    next((vm: any) => {
      vm.initAuthorities()
      if (to.params.login) {
        vm.loadUser(String(to.params.login))
      }
    })
  },
  setup: () => {
    const route = useRoute()
    const { t } = useI18n()
    const router = useRouter()
    const user: IUser = reactive(new User())
    const validator = useVuelidate(userRules, { user })
    const langStore = useLangStore()
    const authorities: IRole[] = reactive([])
    const customToast = useCustomToast()

    const loadUser = async (login: string): Promise<void> => {
      const { data } = await UserManagementService.find(login)
      if (data) {
        Object.assign(user, data)
      }
    }

    const initAuthorities = async (): Promise<void> => {
      const { data } = await RoleService.list()
      if (data) {
        data.forEach(role => {
          authorities.push(role)
        })
      }
    }

    const goBack = (): void => {
      router.back()
    }

    const saveCatalog = (): void => {
      if (route.params.login && user.id) {
        UserManagementService.update(toRaw(user))
          .then(() => {
            customToast.successToast(t('user.actions.update.success'))
            goBack()
          })
          .catch(() => {
            customToast.errorToast(t('user.actions.update.error'))
            goBack()
          })
      } else {
        UserManagementService.save(toRaw(user))
          .then(() => {
            customToast.successToast(t('user.actions.save.success'))
            goBack()
          })
          .catch(() => {
            customToast.errorToast(t('user.actions.save.error'))
            goBack()
          })
      }
    }

    return {
      user,
      validator,
      langStore,
      authorities,
      initAuthorities,
      goBack,
      saveCatalog,
      loadUser,
    }
  },
})
</script>
