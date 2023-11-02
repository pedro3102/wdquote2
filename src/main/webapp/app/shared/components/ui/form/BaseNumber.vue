<template>
  <InputNumber
    v-model="model"
    v-tooltip.top="tooltip"
    :class="invalid ? 'p-invalid' : ''"
    inputClass="text-right"
    v-bind="$attrs"
    @input="emitInput"
  />
</template>

<script lang="ts" setup>
import InputNumber from 'primevue/inputnumber'
import useModel from '@/shared/composables/use-model'

const props = defineProps({
  modelValue: {
    type: Number,
  },
  invalid: {
    type: Boolean,
    default: false,
  },
  tooltip: {
    type: String,
    default: '',
  },
})
const emit = defineEmits(['update:modelValue'])
const { model } = useModel(props, emit)
const emitInput = event => {
  emit('update:modelValue', event.value)
}
</script>
