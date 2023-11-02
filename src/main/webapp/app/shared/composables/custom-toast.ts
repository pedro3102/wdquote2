import { useToast } from 'primevue/usetoast'
import { useI18n } from 'vue-i18n'

export const useCustomToast = () => {
  const toast = useToast()
  const life = 5000
  const { t } = useI18n()

  return {
    successToast: async (detailI18n: string, summaryI18n?: string): Promise<void> => {
      const summaryText = summaryI18n ? t(summaryI18n) : t('components.toast.success')
      const detailText = detailI18n ? t(detailI18n) : ''
      toast.add({ severity: 'success', detail: detailText, life, summary: summaryText })
    },
    infoToast: async (detailI18n: string, summaryI18n?: string): Promise<void> => {
      const summaryText = summaryI18n ? t(summaryI18n) : t('components.toast.info')
      const detailText = detailI18n ? t(detailI18n) : ''
      toast.add({ severity: 'info', detail: detailText, life, summary: summaryText })
    },
    warnToast: async (detailI18n: string, summaryI18n?: string): Promise<void> => {
      const summaryText = summaryI18n ? t(summaryI18n) : t('components.toast.warn')
      const detailText = detailI18n ? t(detailI18n) : ''
      toast.add({ severity: 'warn', detail: detailText, life, summary: summaryText })
    },
    errorToast: async (detailI18n: string, summaryI18n?: string): Promise<void> => {
      const summaryText = summaryI18n ? t(summaryI18n) : t('components.toast.error')
      const detailText = detailI18n ? t(detailI18n) : ''
      toast.add({ severity: 'error', detail: detailText, life, summary: summaryText })
    },
    responseErrorToast: async (status: string): Promise<void> => {
      toast.add({ severity: 'error', detail: t(`error.axios.${status}Description`), life, summary: t(`error.axios.${status}Title`) })
    },
  }
}
