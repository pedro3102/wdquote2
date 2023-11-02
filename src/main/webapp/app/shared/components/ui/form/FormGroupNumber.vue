<template>
  <div class="d-input p-inputgroup flex-1 m-2">
    <span class="p-float-label p-input-icon-right">
      <IconErrorDisplay v-if="checkInvalid" :rules="rules" :customErrorText="customErrorText" />
      <BaseNumber v-model="model" v-bind="$attrs" :invalid="checkInvalid" />
      <label v-if="label" :for="label">{{ label }}</label>
    </span>
  </div>
</template>

<script lang="ts" setup>
import BaseNumber from './BaseNumber.vue'
import useModel from '@/shared/composables/use-model'
import { computed, PropType } from 'vue'
import { ValidationRule } from '@vuelidate/core'
import IconErrorDisplay from '@/shared/components/ui/error/IconErrorDisplay.vue'

const props = defineProps({
  modelValue: {
    type: Number,
  },
  invalid: {
    type: Boolean,
  },
  rules: {
    type: Object as PropType<ValidationRule>,
  },
  label: {
    type: String,
    default: '',
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

<style>
.d-input {
  display: inline;
}
.p-inputnumber.p-invalid.p-component > .p-inputtext {
  padding-right: 25px !important;
  border-color: #ced4da;
}
.p-float-label > .p-invalid + label {
  color: unset;
}
</style>
