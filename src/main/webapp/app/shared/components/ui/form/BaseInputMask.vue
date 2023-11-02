<template>
  <InputMask v-model="model" v-bind="$attrs" v-tooltip="tooltip" :class="invalid ? 'p-invalid' : ''" />
</template>

<script lang="ts" setup>
import InputMask from 'primevue/inputmask'
import useModel from '@/shared/composables/use-model'

const props = defineProps({
  modelValue: {
    type: String,
  },
  invalid: {
    type: Boolean,
    default: false,
  },
  tooltip: {
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
</script>
