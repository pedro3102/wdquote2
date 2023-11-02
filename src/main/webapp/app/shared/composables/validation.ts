import { useI18n } from 'vue-i18n'
import { computed, reactive, ref } from 'vue'

export const useValidation = () => {
  const { t } = useI18n()
  const customErrorText = ref('')
  const rules = reactive<object>({})
  const errorText = computed(() => {
    if (rules?.required?.$invalid) return t('entity.validation.required')
    else if (customErrorText.value) return customErrorText.value
    else if (rules?.minLength?.$invalid) return t('entity.validation.minlength', { min: rules?.minLength?.$params?.min })
    else if (rules?.maxlength?.$invalid) return t('entity.validation.maxlength', { min: rules?.maxlength?.$params?.max })
    else if (rules?.minValue?.$invalid) return t('entity.validation.min', { min: rules?.minValue?.$params?.min })
    else if (rules?.maxValue?.$invalid) return t('entity.validation.max', { max: rules?.maxValue?.$params?.max })
    else if (rules?.email?.$invalid) return t('entity.validation.email')
    else if (rules?.mobile?.$invalid) return t('entity.validation.mobile')
    else if (rules?.sameAs?.$invalid) return t('entity.validation.sameAs')
    else if (rules?.username?.$invalid) return t('entity.validation.username')
    else return
  })

  return {
    errorText,
    customErrorText,
    rules,
  }
}
