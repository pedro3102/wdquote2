import { useConfirm } from 'primevue/useconfirm'
import { useI18n } from 'vue-i18n'

export const useCustomConfirm = () => {
  const confirm = useConfirm()
  const { t } = useI18n()

  const openDeleteConfirm = (group: string, entityLabel: string, accept?: () => void, reject?: () => void) => {
    return confirm.require({
      group: group,
      header: t('entity.delete.title'),
      message: t('entity.delete.question', { entity: entityLabel }),
      acceptLabel: t('entity.action.yesUppercase'),
      rejectLabel: t('entity.action.noUppercase'),
      accept,
      reject,
    })
  }

  const openGenericConfirm = (group: string, title, question: string, accept?: () => void, reject?: () => void) => {
    return confirm.require({
      group: group,
      header: t(title),
      message: t(question),
      acceptLabel: t('entity.action.yesUppercase'),
      rejectLabel: t('entity.action.noUppercase'),
      accept,
      reject,
      onHide: reject,
    })
  }

  const openChangeBooleanStatusConfirm = (group: string, entityLabel: string, accept?: () => void, reject?: () => void) => {
    confirm.require({
      group: group,
      header: t('entity.action.changeStatus.header'),
      message: t('entity.action.changeStatus.message', { user: entityLabel }),
      acceptLabel: t('entity.action.yesUppercase'),
      rejectLabel: t('entity.action.noUppercase'),
      accept,
      reject,
    })
  }

  return {
    openDeleteConfirm,
    openGenericConfirm,
    openChangeBooleanStatusConfirm,
  }
}
