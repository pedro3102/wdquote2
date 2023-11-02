<template>
  <Card>
    <template #title> {{ $t(title) }} </template>
    <template #content>
      <router-view />
    </template>
  </Card>
</template>

<script lang="ts" setup>
import Card from 'primevue/card'
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const title = computed(() => route?.meta?.breadcrumb || '')
</script>
