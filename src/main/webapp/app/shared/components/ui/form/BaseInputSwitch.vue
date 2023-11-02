<template>
  <InputSwitch type="text" v-model="model" v-bind="$attrs" :class="invalid ? 'p-invalid' : ''" />
</template>

<script lang="ts" setup>
import InputSwitch from 'primevue/inputswitch'
import useModel from '@/shared/composables/use-model'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false,
  },
  invalid: {
    type: Boolean,
    default: false,
  },
})
const emit = defineEmits(['update:modelValue'])
const { model } = useModel(props, emit)
</script>
