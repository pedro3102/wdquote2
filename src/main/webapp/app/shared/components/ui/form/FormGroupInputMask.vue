<template>
  <div class="d-input p-inputgroup flex-1 m-2">
    <span class="p-float-label p-input-icon-right">
      <IconErrorDisplay v-if="checkInvalid" :rules="rules" :customErrorText="customErrorText" />
      <BaseInputMask v-model="model" v-bind="$attrs" :uppercase="uppercase" />
      <label v-if="label" :for="label">{{ label }}</label>
    </span>
  </div>
</template>

<script lang="ts" setup>
import BaseInputMask from './BaseInputMask.vue'
import useModel from '@/shared/composables/use-model'
import { computed, PropType } from 'vue'
import { ValidationRule } from '@vuelidate/core'
import IconErrorDisplay from '@/shared/components/ui/error/IconErrorDisplay.vue'

const props = defineProps({
  modelValue: {
    type: String,
  },
  invalid: {
    type: Boolean,
  },
  rules: {
    type: Object as PropType<ValidationRule>,
  },
  customErrorText: {
    type: String,
    default: '',
  },
  label: {
    type: String,
    default: '',
  },
  uppercase: {
    type: Boolean,
    default: null,
  },
})

const emit = defineEmits(['update:modelValue'])

const { model } = useModel(props, emit, { uppercase: props.uppercase })

const checkInvalid = computed(() => {
  return props.invalid || props.rules?.$invalid
})
</script>

<style scoped>
.d-input {
  display: inline;
}
</style>
