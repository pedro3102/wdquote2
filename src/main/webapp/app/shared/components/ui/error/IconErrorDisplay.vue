<template>
  <BaseOverlayPanel v-if="errorText" ref="bop" appendTo="body" :style="{ color: SeverityCodes.DANGER }">
    {{ errorText }}
  </BaseOverlayPanel>
  <span @mouseover="show" @mouseleave="hide" :style="iconStyle">
    <font-awesome-icon v-if="errorText" icon="warning" :color="{ color: SeverityCodes.DANGER }" />
  </span>
</template>

<script lang="ts" setup>
import { PropType, ref, watchEffect } from 'vue'
import { ValidationRule } from '@vuelidate/core'
import { useValidation } from '@/shared/composables'
import { SeverityCodes } from '@/shared/const/entity-constants'
import BaseOverlayPanel from '@/shared/components/ui/overlay-panel/BaseOverlayPanel.vue'

const props = defineProps({
  rules: {
    type: Object as PropType<ValidationRule>,
  },
  customErrorText: {
    type: String,
    default: '',
  },
  iconStyle: {
    type: Object,
    default: () => {},
  },
})

const bop = ref()
const { errorText, customErrorText, rules } = useValidation()
const show = event => {
  bop.value?.op?.show(event)
}
const hide = () => {
  bop.value?.op?.hide()
}

watchEffect(() => (customErrorText.value = props.customErrorText))
watchEffect(() => {
  if (props.rules) Object.assign(rules, props.rules)
})
</script>
<style scoped>
.p-input-icon-right > span {
  position: absolute;
  color: #ef4444;
  right: 0.75rem;
  top: 25%;
  z-index: 1000;
}
</style>
