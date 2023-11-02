<template>
  <Calendar v-model="model" v-bind="$attrs" :class="invalid ? 'p-invalid' : ''" />
</template>

<script lang="ts" setup>
import Calendar from 'primevue/calendar'
import useModel from '@/shared/composables/use-model'

const props = defineProps({
  modelValue: {
    type: Date,
  },
  invalid: {
    type: Boolean,
    default: false,
  },
})
const emit = defineEmits(['update:modelValue'])
const { model } = useModel(props, emit)
</script>
