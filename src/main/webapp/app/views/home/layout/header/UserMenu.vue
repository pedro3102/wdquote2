<template>
  <button @click="toggle" class="p-link layout-topbar-button">
    <font-awesome-icon icon="user" size="xl" />
    <span>Profile</span>
  </button>
  <Menu id="overlay_menu" ref="menu" :model="items" :popup="true">
    <template #start>
      <button @click="profileClick" class="w-full p-link flex align-items-center p-2 pl-3 text-color hover:surface-200 border-noround">
        <Avatar image="https://primefaces.org/cdn/primevue/images/avatar/amyelsner.png" class="mr-2" shape="circle" />
        <div class="flex flex-column align">
          <span class="font-bold">Amy Elsner</span>
          <span class="text-sm">Agent</span>
        </div>
      </button>
    </template>
    <template #item="{ item, label, props }">
      <a class="flex" v-bind="props.action">
        <span v-bind="props.icon" />
        <span v-bind="props.label">{{ label }}</span>
        <Badge v-if="item.badge" class="ml-auto" :value="item.badge" />
      </a>
    </template>
    <template #end>
      <button class="w-full p-link flex align-items-center p-2 pl-4 text-color hover:surface-200 border-noround" @click="executeLogout">
        <i class="pi pi-sign-out" />
        <span class="ml-2">Log Out</span>
      </button>
    </template>
  </Menu>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import Menu from 'primevue/menu'
import Avatar from 'primevue/avatar'
import Badge from 'primevue/badge'
import { inject } from 'vue'
import { AccountService } from '@/store/modules/account/account-service'
import { useRouter } from 'vue-router'
import { loginRouteName } from '@/views/login/route-names'
const accountService = inject<AccountService>('accountService')

const router = useRouter()

const executeLogout = () => {
  accountService.logoutAccount()
  if (router.currentRoute.value.path !== `/${loginRouteName}`) {
    router.push({ name: loginRouteName })
  }
}
const menu = ref()
const items = ref([
  { separator: true },
  {
    label: 'Profile',
    icon: 'pi pi-fw pi-user',
  },
  {
    label: 'Settings',
    icon: 'pi pi-fw pi-cog',
    badge: 2,
  },
  { separator: true },
])

const toggle = event => {
  menu.value.toggle(event)
}
</script>
