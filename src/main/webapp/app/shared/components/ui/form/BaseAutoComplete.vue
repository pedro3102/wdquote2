<template>
  <AutoComplete v-model="model" v-bind="$attrs" :class="`${invalid ? 'p-invalid' : ''} ${dropdownClass}`">
    <template #chip="slotProps">
      <slot name="chip" :data="slotProps" />
    </template>
    <template #header="slotProps">
      <slot name="header" :data="slotProps" />
    </template>
    <template #footer="slotProps">
      <slot name="footer" :data="slotProps" />
    </template>
    <template #item="slotProps">
      <slot name="item" :data="slotProps" />
    </template>
    <template #option="slotProps">
      <slot name="option" :data="slotProps" />
    </template>
    <template #optiongroup="slotProps">
      <slot name="optiongroup" :data="slotProps" />
    </template>
    <template #content="slotProps">
      <slot name="content" :data="slotProps" />
    </template>
    <template #empty="slotProps">
      <slot name="empty" :data="slotProps" />
    </template>
    <template #dropdownicon="slotProps">
      <slot name="dropdownicon" :data="slotProps" />
    </template>
    <template #removetokenicon="slotProps">
      <slot name="removetokenicon" :data="slotProps" />
    </template>
    <template #loadingicon="slotProps">
      <slot name="loadingicon" :data="slotProps" />
    </template>
  </AutoComplete>
</template>

<script lang="ts" setup>
import AutoComplete from 'primevue/autocomplete'
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
