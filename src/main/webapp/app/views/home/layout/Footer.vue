<script setup>
import { useLayout } from '@/shared/composables'
import { computed } from 'vue'

const { layoutConfig } = useLayout()

const logoUrl = computed(() => {
  return `/content/images/${layoutConfig.darkTheme.value ? 'logo-sap-white' : 'logo-sap-dark'}.png`
})
</script>

<template>
  <div class="layout-footer">
    <img :src="logoUrl" alt="Logo" height="20" class="mr-2" />
    SAP
    <span class="font-medium ml-2">Windows & Doors</span>
  </div>
</template>
<style lang="scss" scoped></style>
