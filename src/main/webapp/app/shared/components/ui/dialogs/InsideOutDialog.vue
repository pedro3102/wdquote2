<template>
  <BaseDialog v-model:visible="visible" v-bind="$attrs">
    <slot name="inside"></slot>
  </BaseDialog>
  <slot name="out"></slot>
</template>

<script lang="ts" setup>
import BaseDialog from './BaseDialog'
import { ref, watchEffect } from 'vue'
const props = defineProps({
  show: {
    type: Boolean,
    required: true,
    default: false,
  },
})

const visible = ref(false)

watchEffect(() => (visible.value = props.show))
</script>
