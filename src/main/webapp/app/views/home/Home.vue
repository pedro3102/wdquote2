<template>
  <div class="layout-wrapper" :class="containerClass">
    <Header />
    <div class="layout-sidebar">
      <Sidebar />
    </div>
    <div class="layout-main-container">
      <ContentWrapper />
      <Footer />
    </div>
    <Config />
    <div class="layout-mask"></div>
  </div>
</template>

<script setup>
import { computed, watch, ref } from 'vue'
import Sidebar from '@/views/home/layout/sidebar/SideBar.vue'
import Header from '@/views/home/layout/header/Header.vue'
import Footer from '@/views/home/layout/Footer.vue'
import Config from '@/views/home/layout/Config.vue'
import ContentWrapper from '@/views/home/layout/content/ContentWrapper.vue'
import { useLayout } from '@/shared/composables'

const { layoutConfig, layoutState, isSidebarActive } = useLayout()

const outsideClickListener = ref(null)

watch(isSidebarActive, newVal => {
  if (newVal) {
    bindOutsideClickListener()
  } else {
    unbindOutsideClickListener()
  }
})

const containerClass = computed(() => {
  return {
    'layout-theme-light': layoutConfig.darkTheme.value === 'light',
    'layout-theme-dark': layoutConfig.darkTheme.value === 'dark',
    'layout-overlay': layoutConfig.menuMode.value === 'overlay',
    'layout-static': layoutConfig.menuMode.value === 'static',
    'layout-static-inactive': layoutState.staticMenuDesktopInactive.value && layoutConfig.menuMode.value === 'static',
    'layout-overlay-active': layoutState.overlayMenuActive.value,
    'layout-mobile-active': layoutState.staticMenuMobileActive.value,
    'p-input-filled': layoutConfig.inputStyle.value === 'filled',
    'p-ripple-disabled': !layoutConfig.ripple.value,
  }
})
const bindOutsideClickListener = () => {
  if (!outsideClickListener.value) {
    outsideClickListener.value = event => {
      if (isOutsideClicked(event)) {
        layoutState.overlayMenuActive.value = false
        layoutState.staticMenuMobileActive.value = false
        layoutState.menuHoverActive.value = false
      }
    }
    document.addEventListener('click', outsideClickListener.value)
  }
}
const unbindOutsideClickListener = () => {
  if (outsideClickListener.value) {
    document.removeEventListener('click', outsideClickListener)
    outsideClickListener.value = null
  }
}
const isOutsideClicked = event => {
  const sidebarEl = document.querySelector('.layout-sidebar')
  const topbarEl = document.querySelector('.layout-menu-button')

  return !(
    sidebarEl.isSameNode(event.target) ||
    sidebarEl.contains(event.target) ||
    topbarEl.isSameNode(event.target) ||
    topbarEl.contains(event.target)
  )
}
</script>

<style lang="scss" scoped></style>
