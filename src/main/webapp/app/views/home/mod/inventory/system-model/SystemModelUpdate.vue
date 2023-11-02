<template>
  <FormDialog
    @close="goBack"
    @accept="saveCatalog"
    :header="$t('entity.home.createOrEditLabel', !systemModel?.id ? 1 : 2, { named: { entity: $t('systemModel.detail.title') } })"
    :validate="validator.systemModel.$invalid"
  >
    <div class="formgrid grid pt-2">
      <div class="field col-12 md:col-6">
        <FormGroupInput
          id="field_name"
          v-model="systemModel.name"
          name="field_name"
          :label="$t('systemModel.name')"
          :rules="validator.systemModel.name"
        >
        </FormGroupInput>
      </div>
      <div class="field col-12 md:col-6">
        <FormGroupInput
          id="field_description"
          v-model="systemModel.description"
          name="field_description"
          :label="$t('systemModel.description')"
          :rules="validator.systemModel.description"
        >
        </FormGroupInput>
      </div>
      <div class="field col-12 md:col-6">
        <FormGroupDropdown
          id="field_system"
          v-model="systemModel.system"
          name="field_system"
          :label="$t('systemModel.system')"
          :options="systems"
          optionLabel="name"
          :rules="validator.systemModel.system"
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
import FormGroupInput from '@/shared/components/ui/form/FormGroupInput.vue'
import FormGroupDropdown from '@/shared/components/ui/form/FormGroupDropdown.vue'
import SystemModelService from '@/views/home/mod/inventory/system-model/system-model-service'
import { ISystemModel, SystemModel } from '@/views/home/mod/inventory/system-model/system-model-model'
import { ISystem } from '@/views/home/mod/inventory/system/system-model'
import systemModelRules from '@/views/home/mod/inventory/system-model/system-model-rules'
import { useCustomToast } from '@/shared/composables/custom-toast'
import { useI18n } from 'vue-i18n'
import SystemService from '@/views/home/mod/inventory/system/system-service'

export default defineComponent({
  name: 'SystemModelUpdate',
  components: {
    FormDialog,
    FormGroupInput,
    FormGroupDropdown,
  },
  beforeRouteEnter(to: RouteLocationNormalized, from: RouteLocationNormalized, next) {
    next((vm: any) => {
      if (to.params.id) {
        vm.loadSystemModel(String(to.params.id))
      }
    })
  },
  setup: () => {
    const route = useRoute()
    const { t } = useI18n()
    const router = useRouter()
    const systemModel: ISystemModel = reactive(new SystemModel())
    const validator = useVuelidate(systemModelRules, { systemModel })
    const customToast = useCustomToast()
    const systems: ISystem[] = reactive([])

    const loadSystemModel = async (id: number): Promise<void> => {
      const { data } = await SystemModelService.find(id)
      if (data) {
        Object.assign(systemModel, data)
      }
    }

    const goBack = (): void => {
      router.back()
    }

    SystemService.listBasic().then(res => {
      Object.assign(systems, res.data)
    })

    const saveCatalog = (): void => {
      if (route.params.id && systemModel.id) {
        SystemModelService.update(toRaw(systemModel), systemModel.id)
          .then(() => {
            customToast.successToast(t('systemModel.actions.update.success'))
            goBack()
          })
          .catch(() => {
            customToast.errorToast(t('systemModel.actions.update.error'))
            goBack()
          })
      } else {
        SystemModelService.save(toRaw(systemModel))
          .then(() => {
            customToast.successToast(t('systemModel.actions.save.success'))
            goBack()
          })
          .catch(() => {
            customToast.errorToast(t('systemModel.actions.save.error'))
            goBack()
          })
      }
    }

    return {
      systemModel,
      validator,
      systems,
      goBack,
      saveCatalog,
      loadSystemModel,
    }
  },
})
</script>
