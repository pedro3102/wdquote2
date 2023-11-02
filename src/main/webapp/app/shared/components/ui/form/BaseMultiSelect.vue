<template>
  <MultiSelect
    v-model="model"
    :options="options"
    class="w-full md:w-14rem"
    v-bind="$attrs"
    :class="`${invalid ? 'p-invalid' : ''} ${dropdownClass}`"
  >
    <template #optiongroup="slotProps">
      <slot name="optiongroup" :data="slotProps" />
    </template>
    <template #value="slotProps">
      <slot name="value" :data="slotProps" />
    </template>
    <template #option="slotProps">
      <slot name="option" :data="slotProps" />
    </template>
    <template #footer="slotProps">
      <slot name="footer" :data="slotProps" />
    </template>
  </MultiSelect>
</template>

<script lang="ts" setup>
import MultiSelect from 'primevue/multiselect'
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
  options: {
    type: Array,
    default: () => [],
  },
})
const emit = defineEmits(['update:modelValue'])
const { model } = useModel(props, emit)
</script>
