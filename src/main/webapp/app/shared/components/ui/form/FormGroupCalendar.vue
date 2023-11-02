<template>
  <div class="d-input p-inputgroup flex-1 m-2">
    <span class="p-float-label p-input-icon-right">
      <IconErrorDisplay v-if="checkInvalid" :rules="rules" :customErrorText="customErrorText" />
      <BaseCalendar v-model="model" :invalid="checkInvalid" v-bind="$attrs" />
      <label v-if="label" :for="label">{{ label }}</label>
    </span>
  </div>
</template>

<script lang="ts" setup>
import BaseCalendar from './BaseCalendar.vue'
import useModel from '@/shared/composables/use-model'
import { computed, PropType } from 'vue'
import { ValidationRule } from '@vuelidate/core'
import IconErrorDisplay from '@/shared/components/ui/error/IconErrorDisplay.vue'

const props = defineProps({
  modelValue: {
    type: Date,
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
})

const emit = defineEmits(['update:modelValue'])

const { model } = useModel(props, emit)

const checkInvalid = computed(() => {
  return props.invalid || props.rules?.$invalid
})
</script>

<style scoped>
.d-input {
  display: inline;
}
</style>
