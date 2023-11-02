<template>
  <div class="d-input p-inputgroup flex-1 m-2">
    <span class="p-float-label p-input-icon-right">
      <IconErrorDisplay v-if="checkInvalid" :rules="rules" :customErrorText="customErrorText" />
      <BaseAutoComplete v-model="model" v-bind="$attrs">
        <template #chip="{ data }">
          <slot name="chip" :data="data" />
        </template>
        <template #header="{ data }">
          <slot name="header" :data="data" />
        </template>
        <template #footer="{ data }">
          <slot name="footer" :data="data" />
        </template>
        <template #item="{ data }">
          <slot name="item" :data="data" />
        </template>
        <template #option="{ data }">
          <slot name="option" :data="data" />
        </template>
        <template #optiongroup="{ data }">
          <slot name="optiongroup" :data="data" />
        </template>
        <template #content="{ data }">
          <slot name="content" :data="data" />
        </template>
        <template #empty="{ data }">
          <slot name="empty" :data="data" />
        </template>
        <template #dropdownicon="{ data }">
          <slot name="dropdownicon" :data="data" />
        </template>
        <template #removetokenicon="{ data }">
          <slot name="removetokenicon" :data="data" />
        </template>
        <template #loadingicon="{ data }">
          <slot name="loadingicon" :data="data" />
        </template>
      </BaseAutoComplete>
      <label v-if="label" :for="label">{{ label }}</label>
    </span>
  </div>
</template>

<script lang="ts" setup>
import BaseAutoComplete from './BaseAutoComplete.vue'
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
