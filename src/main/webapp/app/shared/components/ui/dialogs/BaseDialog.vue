<template>
  <Dialog v-bind="$attrs">
    <slot></slot>
    <template #footer>
      <slot name="customFooter"> </slot>
    </template>
  </Dialog>
</template>

<script lang="ts" setup>
import Dialog from 'primevue/dialog'
</script>
