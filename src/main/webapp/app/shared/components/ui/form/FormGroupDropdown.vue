<template>
  <div class="d-input p-inputgroup flex-1 m-2">
    <span class="p-float-label p-input-icon-right">
      <IconErrorDisplay v-if="checkInvalid" :rules="rules" :customErrorText="customErrorText" :iconStyle="{ right: '2rem' }" />
      <BaseDropdown v-model="model" v-bind="$attrs">
        <template #header="slotProps">
          <slot :data="slotProps.data" name="header" />
        </template>
        <template #optiongroup="slotProps">
          <slot :data="slotProps.data" name="optiongroup" />
        </template>
        <template #value="slotProps">
          <slot :data="slotProps.data" name="value" />
        </template>
        <template #option="slotProps">
          <slot :data="slotProps.data" name="option" />
        </template>
      </BaseDropdown>
      <label v-if="label" :for="label">{{ label }}</label>
    </span>
  </div>
</template>

<script lang="ts" setup>
import BaseDropdown from './BaseDropdown.vue'
import useModel from '@/shared/composables/use-model'
import { computed, PropType } from 'vue'
import { ValidationRule } from '@vuelidate/core'
import IconErrorDisplay from '@/shared/components/ui/error/IconErrorDisplay.vue'

const props = defineProps({
  modelValue: {
    type: [Number, String, Object],
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

<style scoped>
.d-input {
  display: inline;
}
</style>
