<template>
  <div class="d-input p-inputgroup flex-1">
    <span class="p-float-label p-input-icon-right">
      <font-awesome-icon v-if="checkInvalid" icon="warning" />
      <slot></slot>
      <label v-if="label" :for="label">{{ label }}</label>
    </span>
  </div>
</template>

<script lang="ts" setup>
import { computed, PropType } from 'vue'
import { ValidationRule } from '@vuelidate/core'

const props = defineProps({
  label: {
    type: String,
    default: '',
  },
  rules: {
    type: Object as PropType<ValidationRule>,
  },
  customErrorText: {
    type: String,
    default: '',
  },
})
const checkInvalid = computed(() => {
  return props.rules?.$invalid
})
</script>

<style scoped>
.p-input-icon-right > svg:last-of-type {
  color: #ef4444;
}
.d-input {
  display: inline;
}
</style>
