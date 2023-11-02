<template>
  <ActionsDialog :show="show" :header="header" :validate="validate" @close="onClose" @accept="onAccept" @hide="onHide">
    <BaseForm name="editForm" role="form" @submit.stop.prevent>
      <slot />
    </BaseForm>
  </ActionsDialog>
</template>
<script lang="ts" setup>
import { ref, PropType, watch } from 'vue'
import ActionsDialog from '@/shared/components/ui/dialogs/ActionsDialog.vue'
import BaseForm from '@/shared/components/ui/form/BaseForm.vue'
import { useRouter } from 'vue-router'
import { EditingStatus } from '@/shared/const/entity-constants'

const emit = defineEmits(['accept', 'close'])
const props = defineProps({
  header: {
    type: String,
    default: '',
  },
  validate: {
    type: Boolean,
    default: false,
  },
  editingStatus: {
    type: String as PropType<EditingStatus>,
    default: EditingStatus.NONE,
  },
})

watch(
  () => props.editingStatus,
  () => {
    if (props.editingStatus === EditingStatus.SAVED) {
      close()
    }
  }
)

const router = useRouter()
const show = ref(true)

const close = () => {
  show.value = false
  emit('update:editingStatus', EditingStatus.NONE)
}

const onAccept = () => {
  emit('accept')
}
const onClose = () => {
  emit('close')
  close()
}
const onHide = () => {
  if (show.value) {
    emit('close')
    close()
  }
}
</script>
