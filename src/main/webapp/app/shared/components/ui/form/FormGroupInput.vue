<template>
  <div class="d-input p-inputgroup flex-1 m-2">
    <span class="p-float-label p-input-icon-right">
      <IconErrorDisplay v-if="checkInvalid" :rules="rules" :customErrorText="customErrorText" />
      <BaseInput v-model="model" v-bind="$attrs" :maxlength="maxValue" :uppercase="uppercase" />
      <label v-if="label" :for="label">{{ label }}</label>
    </span>
    <BaseButton v-if="addonIcon" :severity="addonColor" @click="addonClick">
      <font-awesome-icon :icon="addonIcon" />
    </BaseButton>
  </div>
</template>

<script lang="ts" setup>
import BaseInput from './BaseInput.vue'
import useModel from '@/shared/composables/use-model'
import { computed, PropType } from 'vue'
import { ValidationRule } from '@vuelidate/core'
import BaseButton from '@/shared/components/ui/buttons/BaseButton.vue'
import IconErrorDisplay from '@/shared/components/ui/error/IconErrorDisplay.vue'

const props = defineProps({
  modelValue: {
    type: String,
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
  addonColor: {
    type: String,
    default: '',
  },
  addonIcon: {
    type: String,
    default: '',
  },
  uppercase: {
    type: Boolean,
    default: null,
  },
})
const emit = defineEmits(['update:modelValue', 'addon-click'])

const addonClick = event => {
  emit('addon-click', event)
}

const { model } = useModel(props, emit, { uppercase: props.uppercase })
const maxValue = computed(() => props.rules?.maxLength?.$params?.max)

const checkInvalid = computed(() => {
  return props.invalid || props.rules?.$invalid
})
</script>
<style scoped>
.d-input {
  display: inline;
}
</style>
