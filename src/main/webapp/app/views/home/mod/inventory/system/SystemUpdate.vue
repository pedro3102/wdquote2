<template>
  <FormDialog
    :style="{ width: '30vw' }"
    @close="goBack"
    @accept="saveCatalog"
    :header="$t('entity.home.createOrEditLabel', !system?.id ? 1 : 2, { named: { entity: $t('system.detail.title') } })"
    :validate="validator.system.$invalid"
  >
    <div class="formgrid grid pt-2">
      <div class="field col-12 md:col-12">
        <FormGroupInput id="field_name" v-model="system.name" name="field_name" :label="$t('system.name')" :rules="validator.system.name">
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
import SystemService from '@/views/home/mod/inventory/system/system-service'
import { ISystem, System } from '@/views/home/mod/inventory/system/system-model'
import systemRules from '@/views/home/mod/inventory/system/system-rules'
import { useCustomToast } from '@/shared/composables/custom-toast'
import { useI18n } from 'vue-i18n'

export default defineComponent({
  name: 'SystemUpdate',
  components: {
    FormDialog,
    FormGroupInput,
  },
  beforeRouteEnter(to: RouteLocationNormalized, from: RouteLocationNormalized, next) {
    next((vm: any) => {
      if (to.params.id) {
        vm.loadSystem(String(to.params.id))
      }
    })
  },
  setup: () => {
    const route = useRoute()
    const { t } = useI18n()
    const router = useRouter()
    const system: ISystem = reactive(new System())
    const validator = useVuelidate(systemRules, { system })
    const customToast = useCustomToast()

    const loadSystem = async (id: number): Promise<void> => {
      const { data } = await SystemService.find(id)
      if (data) {
        Object.assign(system, data)
      }
    }

    const goBack = (): void => {
      router.back()
    }

    const saveCatalog = (): void => {
      if (route.params.id && system.id) {
        SystemService.update(toRaw(system), system.id)
          .then(() => {
            customToast.successToast(t('system.actions.update.success'))
            goBack()
          })
          .catch(() => {
            customToast.errorToast(t('system.actions.update.error'))
            goBack()
          })
      } else {
        SystemService.save(toRaw(system))
          .then(() => {
            customToast.successToast(t('system.actions.save.success'))
            goBack()
          })
          .catch(() => {
            customToast.errorToast(t('system.actions.save.error'))
            goBack()
          })
      }
    }

    return {
      system: system,
      validator,
      goBack,
      saveCatalog,
      loadSystem,
    }
  },
})
</script>
