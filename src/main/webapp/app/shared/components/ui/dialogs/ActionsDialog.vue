<template>
  <BaseDialog v-model:visible="visible" :style="{ width: '50vw' }" v-bind="$attrs">
    <slot></slot>
    <template #customFooter>
      <slot name="customFooter">
        <SaveCancel
          :submit="emitAccept"
          :cancel="emitClose"
          :validate="validate"
          :submitText="acceptLabel || $t('entity.action.save')"
          :cancelText="closeLabel || $t('entity.action.cancel')"
        />
      </slot>
    </template>
  </BaseDialog>
</template>

<script lang="ts" setup>
import BaseDialog from './BaseDialog'
import SaveCancel from '@/shared/components/ui/buttons/SaveCancel.vue'
import { ref, watchEffect } from 'vue'

const props = defineProps({
  show: {
    type: Boolean,
    required: true,
    default: false,
  },
  acceptLabel: {
    type: String,
    default: '',
  },
  closeLabel: {
    type: String,
    default: '',
  },
  validate: {
    type: Boolean,
    default: false,
  },
})
const emit = defineEmits(['accept', 'close'])

const visible = ref(false)

const emitAccept = () => {
  emit('accept')
}
const emitClose = () => {
  emit('close')
}

watchEffect(() => (visible.value = props.show))
</script>
