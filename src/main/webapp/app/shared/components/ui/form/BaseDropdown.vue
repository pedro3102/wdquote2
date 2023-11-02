<template>
  <Dropdown v-model="model" :class="`${invalid ? 'p-invalid' : ''} ${dropdownClass}`" class="w-full md:w-14rem" v-bind="$attrs">
    <template #header="slotProps">
      <slot :data="slotProps" name="header" />
    </template>
    <template #optiongroup="slotProps">
      <slot :data="slotProps" name="optiongroup" />
    </template>
    <template #value="slotProps">
      <slot :data="slotProps" name="value" />
    </template>
    <template #option="slotProps">
      <slot :data="slotProps" name="option" />
    </template>
  </Dropdown>
</template>

<script lang="ts" setup>
import Dropdown from 'primevue/dropdown'
import useModel from '@/shared/composables/use-model'

const props = defineProps({
  modelValue: {
    type: [Number, String, Object],
    default: null,
  },
  dropdownClass: {
    type: String,
    default: 'w-full md:w-14rem',
  },
  invalid: {
    type: Boolean,
    default: false,
  },
})

const emit = defineEmits(['update:modelValue'])
const { model } = useModel(props, emit)
</script>
