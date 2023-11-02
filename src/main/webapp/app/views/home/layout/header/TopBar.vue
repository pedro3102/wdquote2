<template>
  <div class="layout-topbar">
    <router-link to="/dashboard" class="layout-topbar-logo">
      <img :src="logoUrl" alt="logo" />
      <span>SAP Windows & Doors</span>
    </router-link>
    <button class="p-link layout-menu-button layout-topbar-button" @click="onMenuToggle()">
      <!--            <i class="pi pi-bars"></i>-->
      <font-awesome-icon icon="bars" size="xl" />
    </button>

    <button class="p-link layout-topbar-menu-button layout-topbar-button" @click="null">
      <i class="pi pi-ellipsis-v"></i>
    </button>

    <div class="layout-topbar-menu">
      <LanguageMenu />
      <button @click="null" class="p-link layout-topbar-button">
        <font-awesome-icon icon="calendar-alt" size="xl" />
        <span>Calendar</span>
      </button>
      <UserMenu />
      <button @click="null" class="p-link layout-topbar-button">
        <font-awesome-icon icon="cog" size="xl" />
        <span>Settings</span>
      </button>
    </div>
  </div>
</template>
<script setup>
import { ref, computed, onMounted, onBeforeUnmount } from 'vue'
import { useLayout } from '@/shared/composables'
import { useRouter } from 'vue-router'
import UserMenu from './UserMenu.vue'
import LanguageMenu from './LanguageMenu.vue'

const { layoutConfig, onMenuToggle } = useLayout()

const outsideClickListener = ref(null)
const topbarMenuActive = ref(false)
const router = useRouter()

onMounted(() => {
  // bindOutsideClickListener();
})

onBeforeUnmount(() => {
  // unbindOutsideClickListener();
})

const logoUrl = computed(() => {
  return `/content/images/logo-sap-dark.png`
})

/*const bindOutsideClickListener = () => {
  if (!outsideClickListener.value) {
    outsideClickListener.value = event => {
      if (isOutsideClicked(event)) {
        topbarMenuActive.value = false;
      }
    };
    document.addEventListener('click', outsideClickListener.value);
  }
};
const unbindOutsideClickListener = () => {
  if (outsideClickListener.value) {
    document.removeEventListener('click', outsideClickListener);
    outsideClickListener.value = null;
  }
};*/
const isOutsideClicked = event => {
  if (!topbarMenuActive.value) return

  const sidebarEl = document.querySelector('.layout-topbar-menu')
  const topbarEl = document.querySelector('.layout-topbar-menu-button')

  return !(
    sidebarEl.isSameNode(event.target) ||
    sidebarEl.contains(event.target) ||
    topbarEl.isSameNode(event.target) ||
    topbarEl.contains(event.target)
  )
}
</script>
<style lang="scss" scoped></style>
