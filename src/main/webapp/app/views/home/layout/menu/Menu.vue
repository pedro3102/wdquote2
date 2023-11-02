<template>
  <ul class="layout-menu">
    <template v-for="(item, i) in menuItems">
      <MenuItem v-if="!item.separator" :key="i" :menu="item" :index="i" />
      <li v-if="item.separator" :key="i" class="menu-separator"></li>
    </template>
  </ul>
</template>
<script lang="ts" setup>
import { computed, ref } from 'vue'

import MenuItem from './MenuItem.vue'
import { IMenuItem } from '@/store/modules/menu/menu-store-model'
import { useMenuStore } from '@/store/modules/menu/menu-store'

const menuStore = useMenuStore()

const menuItems = computed<IMenuItem[]>(() => menuStore.getMenu)
</script>
