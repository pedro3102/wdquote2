<template>
  <li v-hasAccess.authority.static="menu" :class="{ 'layout-root-menuitem': root, 'active-menuitem': isActiveMenu }">
    <div v-if="root && menu.visible !== false" class="layout-menuitem-root-text">{{ $t(menu.label) }}</div>
    <a
      v-if="(!menu.to || menu.items?.length > 0) && menu.visible !== false"
      :href="menu.url"
      @click="itemClick($event, menu)"
      :class="menu.class"
      :target="menu.target"
      tabindex="0"
    >
      <font-awesome-icon v-if="menu.icon" class="layout-menuitem-icon" :icon="menu.icon" />
      <span class="layout-menuitem-text">{{ $t(menu.label) }}</span>
      <font-awesome-icon class="layout-submenu-toggler" v-if="menu.items" icon="fa-angle-down" />
    </a>
    <router-link
      v-if="menu.to && (!menu.items || menu.items.length === 0) && menu.visible !== false"
      @click="itemClick($event, menu)"
      :class="[menu.class, { 'active-route': checkActiveRoute(menu) }]"
      tabindex="0"
      :to="menu.to"
    >
      <font-awesome-icon v-if="menu.icon" class="layout-menuitem-icon" :icon="menu.icon" />
      <span class="layout-menuitem-text">{{ $t(menu.label) }}</span>
      <font-awesome-icon class="layout-submenu-toggler" v-if="menu.items?.length > 0" icon="fa-angle-down" />
    </router-link>
    <Transition v-if="menu.items?.length > 0 && menu.visible !== false" name="layout-submenu">
      <ul v-show="root ? true : isActiveMenu" class="layout-submenu">
        <MenuItem v-for="(child, i) in menu.items" :key="child" :index="i" :menu="child" :parentItemKey="itemKey" :root="false" />
      </ul>
    </Transition>
  </li>
</template>
<script lang="ts" setup>
import { ref, onBeforeMount, watch, PropType } from 'vue'
import { useRoute } from 'vue-router'
import { useLayout } from '@/shared/composables'
import { IMenuItem } from '@/store/modules/menu/menu-store-model'

const route = useRoute()

const { layoutConfig, layoutState, setActiveMenuItem, onMenuToggle } = useLayout()

const props = defineProps({
  menu: {
    type: Object as PropType<IMenuItem>,
    default: () => ({}),
  },
  index: {
    type: Number,
    default: 0,
  },
  root: {
    type: Boolean,
    default: true,
  },
  parentItemKey: {
    type: String,
    default: null,
  },
})

const isActiveMenu = ref(false)
const itemKey = ref('')

onBeforeMount(() => {
  itemKey.value = props.parentItemKey ? props.parentItemKey + '-' + props.index : String(props.index)

  const activeItem = layoutConfig.activeMenuItem.value
  isActiveMenu.value = activeItem === itemKey.value || activeItem ? activeItem.startsWith(itemKey.value + '-') : false
})

watch(
  () => layoutConfig.activeMenuItem.value,
  newVal => {
    isActiveMenu.value = newVal === itemKey.value || newVal?.startsWith(itemKey.value + '-')
  }
)
const itemClick = (event, item) => {
  if (item.disabled) {
    event.preventDefault()
    return
  }

  const { overlayMenuActive, staticMenuMobileActive } = layoutState

  if ((item.to || item.url) && (staticMenuMobileActive.value || overlayMenuActive.value)) {
    onMenuToggle()
  }

  if (item.command) {
    item.command({ originalEvent: event, menu: item })
  }

  const foundItemKey = item.items ? (isActiveMenu.value ? props.parentItemKey : itemKey) : itemKey.value

  setActiveMenuItem(foundItemKey)
}

const checkActiveRoute = item => {
  return route.path === item.to
}
</script>
