<template>
  <Button :class="cancelClass" @click="useCancel" text>
    <font-awesome-icon :class="cancelIconClass" :icon="cancelIcon" />
    <span>{{ cancelText }}</span>
  </Button>
  <Button :class="submitClass" :disabled="validate || isSaving || submitDisabled" @click="useSubmit">
    <font-awesome-icon :class="submitIconClass" :icon="submitIcon" />
    <span>{{ submitText }}</span>
  </Button>
</template>
<script lang="ts" setup>
import { ref } from 'vue'
import Button from 'primevue/button'

const props = defineProps({
  validate: {
    type: Boolean,
    default: false,
  },
  submit: {
    type: Function,
  },
  submitDisabled: {
    type: Boolean,
    default: false,
  },
  submitText: {
    type: String,
    default: 'Save',
  },
  submitIcon: {
    type: String,
    default: 'check',
  },
  submitClass: {
    type: String,
    default: '',
  },
  submitIconClass: {
    type: String,
    default: 'mr-1',
  },
  cancelIconClass: {
    type: String,
    default: 'mr-1',
  },
  cancel: {
    type: Function,
  },
  cancelIcon: {
    type: String,
    default: 'times',
  },
  cancelClass: {
    type: String,
    default: '',
  },
  cancelText: {
    type: String,
    default: 'Cancel',
  },
})
const emit = defineEmits(['submit', 'cancel'])
const isSaving = ref(false)

const useCancel = () => {
  if (props.cancel) {
    props.cancel()
  }
  emit('cancel')
}

const useSubmit = () => {
  // isSaving.value = true
  if (props.submit) {
    props.submit()
    // isSaving.value = false
  }
  emit('submit')
}
</script>
