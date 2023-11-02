<template>
  <Breadcrumb :model="children" class="mb-3">
    <template #item="{ label, item, props }">
      <router-link v-if="item.route" v-slot="routerProps" :to="item.to" custom>
        <a :href="routerProps.href" v-bind="props.action">
          <font-awesome-icon v-if="item.icon" :icon="item.icon" class="m-1" />
          <span v-if="label !== 'Home'" v-bind="props.label">{{ $t(label) }}</span>
        </a>
      </router-link>
      <a v-else :href="item.path" :target="item.target" v-bind="props.action">
        <font-awesome-icon v-if="item.icon" :icon="item.icon" />
        <span v-if="label !== 'Home'" v-bind="props.label">{{ $t(label) }}</span>
      </a>
    </template>
  </Breadcrumb>
  <!--  <ol class="breadcrumb float-sm-right">
    <li
      v-for="child in children"
      :key="child.text"
      class="breadcrumb-item"
    >
      <router-link
        v-if="!child.active"
        :to="child.to"
      >
        {{ $t(child.text) }}
      </router-link>
      {{ child.active ? $t(child.text) : '' }}
    </li>
  </ol>-->
</template>

<script lang="ts" setup>
import { computed } from 'vue'
import Breadcrumb from 'primevue/breadcrumb'

const props = defineProps({
  items: {
    type: Array,
    default: () => [],
  },
})

const children = computed(() => {
  if (props.items.length > 0) {
    return props.items
  }
  return []
})
</script>

<style lang="css">
.p-breadcrumb {
  background: none;
  border: none;
  padding: 0;
}
</style>
