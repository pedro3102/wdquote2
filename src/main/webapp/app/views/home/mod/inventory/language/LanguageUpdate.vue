<template>
  <FormDialog
    :style="{ width: '30vw' }"
    :breakpoints="{ '1264px': '40vw', '960px': '50vw', '641px': '70vw' }"
    @close="goBack"
    @accept="saveCatalog"
    :header="$t('entity.home.createOrEditLabel', !language?.id ? 1 : 2, { named: { entity: $t('language.detail.title') } })"
    :validate="validator.language.$invalid"
  >
    <div class="formgrid grid pt-2">
      <div class="field col-12 md:col-3">
        <FormGroupInput
          id="field_code"
          v-model="language.code"
          name="field_code"
          :label="$t('language.code')"
          :rules="validator.language.code"
          :uppercase="true"
        />
      </div>
      <div class="field col-12 md:col-9">
        <FormGroupInput
          id="field_name"
          v-model="language.name"
          name="field_name"
          :label="$t('language.name')"
          :rules="validator.language.name"
        />
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
import LanguageService from '@/views/home/mod/inventory/language/language-service'
import { ILanguage, Language } from '@/views/home/mod/inventory/language/language-model'
import languageRules from '@/views/home/mod/inventory/language/language-rules'
import { useCustomToast } from '@/shared/composables/custom-toast'
import { useI18n } from 'vue-i18n'

export default defineComponent({
  name: 'LanguageUpdate',
  components: {
    FormDialog,
    FormGroupInput,
  },
  beforeRouteEnter(to: RouteLocationNormalized, from: RouteLocationNormalized, next) {
    next((vm: any) => {
      if (to.params.id) {
        vm.loadLanguage(String(to.params.id))
      }
    })
  },
  setup: () => {
    const route = useRoute()
    const { t } = useI18n()
    const router = useRouter()
    const language: ILanguage = reactive(new Language())
    const validator = useVuelidate(languageRules, { language })
    const customToast = useCustomToast()

    const loadLanguage = async (id: number): Promise<void> => {
      const { data } = await LanguageService.find(id)
      if (data) {
        Object.assign(language, data)
      }
    }

    const goBack = (): void => {
      router.back()
    }

    const saveCatalog = (): void => {
      if (route.params.id && language.id) {
        LanguageService.update(toRaw(language), language.id)
          .then(() => {
            customToast.successToast(t('language.actions.update.success'))
            goBack()
          })
          .catch(() => {
            customToast.errorToast(t('language.actions.update.error'))
            goBack()
          })
      } else {
        LanguageService.save(toRaw(language))
          .then(() => {
            customToast.successToast(t('language.actions.save.success'))
            goBack()
          })
          .catch(() => {
            customToast.errorToast(t('language.actions.save.error'))
            goBack()
          })
      }
    }

    return {
      language: language,
      validator,
      goBack,
      saveCatalog,
      loadLanguage,
    }
  },
})
</script>
