<template>
  <Button v-bind="$attrs">
    <slot>
      <font-awesome-icon v-if="icon" :class="iconClass" :icon="icon" :spin="iconSpin" />
      <span v-if="label" :class="labelClass">{{ label }}</span>
    </slot>
  </Button>
</template>
<script lang="ts" setup>
import Button from 'primevue/button'

const props = defineProps({
  icon: {
    type: String,
    default: '',
  },
  iconClass: {
    type: String,
    default: '',
  },
  labelClass: {
    type: String,
    default: '',
  },
  label: {
    type: String,
    default: '',
  },
  iconSpin: {
    type: Boolean,
    default: false,
  },
})
</script>
