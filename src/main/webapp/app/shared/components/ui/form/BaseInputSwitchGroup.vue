<template>
  <div class="p-inputgroup flex-1">
    <div class="flex align-content-start">
      <slot></slot>
      <label class="ml-2" v-if="label" :for="label">{{ label }}</label>
    </div>
  </div>
</template>

<script lang="ts" setup>
const props = defineProps({
  label: {
    type: String,
    default: '',
  },
  rules: {
    type: Object,
    default: () => {},
  },
  customErrorText: {
    type: String,
    default: '',
  },
})
</script>
