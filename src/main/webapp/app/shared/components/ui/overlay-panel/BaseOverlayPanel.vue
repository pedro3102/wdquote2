<template>
  <OverlayPanel ref="op" v-bind="$attrs">
    <slot></slot>
  </OverlayPanel>
</template>

<script lang="ts" setup>
import OverlayPanel from 'primevue/overlaypanel'
import { ref } from 'vue'

const op = ref()

defineExpose({
  op,
})
</script>
