<template>
  <BreadCrumb v-if="showCrumbs" :items="crumbs" />
</template>

<script lang="ts" setup>
import { computed, ref } from 'vue'
import { useRoute } from 'vue-router'
import BreadCrumb from '@/shared/components/ui/BreadCrumb.vue'

const route = useRoute()
const finalBreadcrumb = ref([])

const showCrumbs = computed(() => crumbs.value.length > 0)

const addBreadcrumb = obj => {
  const newBreadC = {
    label: obj.meta.breadcrumb,
    to: { name: obj.name },
    active: obj.name === route.name,
    path: obj.path,
    route: !!obj.path,
    icon: obj.meta.breadcrumbIcon,
  }
  const pos = finalBreadcrumb.value.findIndex(item => item.path === obj.path)
  if (pos === -1) {
    finalBreadcrumb.value.push(newBreadC)
  } else {
    finalBreadcrumb.value[pos] = newBreadC
  }
}

const resetFinalBreadcrumb = () => {
  finalBreadcrumb.value = []
}

const crumbs = computed(() => {
  resetFinalBreadcrumb()
  route.matched.forEach(obj => {
    if (obj?.meta?.breadcrumb || obj?.meta?.breadcrumbIcon) {
      addBreadcrumb(obj)
    }
  })

  return finalBreadcrumb.value.length > 1 ? finalBreadcrumb.value : []
})
</script>
