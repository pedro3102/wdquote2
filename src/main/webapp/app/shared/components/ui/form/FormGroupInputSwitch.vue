<template>
  <BaseInputSwitchGroup class="m-2" v-bind="$props" :rules="rules">
    <BaseInputSwitch v-model="model" v-bind="$attrs" :invalid="checkInvalid" />
    <template #customErrors>
      <slot name="customErrors" />
    </template>
  </BaseInputSwitchGroup>
</template>

<script lang="ts" setup>
import BaseInputSwitch from './BaseInputSwitch.vue'
import BaseInputSwitchGroup from './BaseInputSwitchGroup.vue'
import useModel from '@/shared/composables/use-model'
import { computed, PropType } from 'vue'
import { ValidationRule } from '@vuelidate/core'

const props = defineProps({
  modelValue: {
    type: String,
  },
  invalid: {
    type: Boolean,
  },
  size: {
    type: String,
  },
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
const emit = defineEmits(['update:modelValue'])

const { model } = useModel(props, emit)

const checkInvalid = computed(() => {
  return props.invalid || props.rules?.$invalid
})
</script>
