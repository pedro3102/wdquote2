<template>
  <span>
    <BaseConfirmDialog group="general" />
    <Toast position="bottom-right" />
    <router-view />
  </span>
</template>

<script lang="ts">
import Toast from 'primevue/toast'
import { defineComponent } from 'vue'
import BaseConfirmDialog from '@/shared/components/ui/dialogs/BaseConfirmDialog.vue'

export default defineComponent({
  name: 'App',
  components: { Toast, BaseConfirmDialog },
})
</script>
