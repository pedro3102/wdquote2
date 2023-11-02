import { defineStore } from 'pinia'
import { IMenu, IMenuItem } from '@/store/modules/menu/menu-store-model'
import { useAuthStore } from '@/store/modules/auth/auth-store'
import { circleIcon, dotCircleIcon } from '@/store/modules/menu/menu-constants'
import { IAuthResource } from '@/store/modules/auth/auth-resource-model'
import { toRaw } from 'vue'
import { IUser } from '@/views/home/admin/user-management/user.model'
import { RESOURCES } from '@/shared/const/auth-constants'

const loadAllMenus = (access: IAuthResource) => {
  const menus: IMenuItem[] = []
  const requireMenus = require.context('./menus', true, /[\w-]+\.ts$/)
  requireMenus.keys().forEach(key => menus.push(requireMenus(key).default(access)))
  return menus
}

const collapseMenu = element => {
  Object.assign(element, {
    open: false,
  })
}

const expandMenu = element => {
  Object.assign(element, {
    open: true,
  })
}

const selectItem = item => {
  Object.assign(item, {
    icon: dotCircleIcon,
  })
}

const unselectItem = item => {
  Object.assign(item, {
    icon: circleIcon,
  })
}

const checkMatched = (item, matched) => {
  if (matched) {
    return matched.find(match => match.name === item.id)
  }
  return false
}

const setStatusToChild = (element, payload, dashboardMenu) => {
  element.items.forEach(item => {
    if (item.id === payload.itemId || checkMatched(item, payload.matched)) {
      // If one found we collapse the Dashboard
      collapseMenu(dashboardMenu)
      expandMenu(element)
      selectItem(item)
    } else {
      // collapseMenu(element);
      unselectItem(item)
    }
  })
}

const loadMainMenu = async (currentUser: IUser, authResource: IAuthResource): Promise<IMenuItem[]> =>
  Promise.resolve(!currentUser ? [] : loadAllMenus(authResource))

const toggleSubMenu = (menu: IMenuItem, items: IMenuItem[] = []) => {
  items.forEach(element => {
    if (element.id === menu.id) {
      // && element.class !== 'active') {
      if (element.open) collapseMenu(element)
      else expandMenu(element)
      return true
    }
    return toggleSubMenu(menu, element.items)
  })
  return false
}

export const initialMenuState: IMenu = {
  menu: [],
}

export const useMenuStore = defineStore({
  id: 'resource-menu',
  state: () => initialMenuState,
  getters: {
    getMenu: (state: IMenu): IMenuItem[] => state.menu,
    getMenuAccess:
      () =>
      (action: string): string => {
        const authStore = useAuthStore()
        const resource: IAuthResource = toRaw(authStore.getResource(RESOURCES.RESOURCE_MAIN_MENU))
        if (resource?.actions && resource.actions[action] && resource.actions[action].roles) {
          return resource.actions[action].roles.join()
        }
        return ''
      },
  },
  actions: {
    async loadMenu(user: IUser): Promise<void> {
      const authStore = useAuthStore()
      this.menu = await loadMainMenu(user, authStore.getResource(RESOURCES.RESOURCE_MAIN_MENU))
    },
    toggleMenu(menu: IMenuItem): void {
      toggleSubMenu(menu, this.menu)
    },
    selectMenuItem(payload): void {
      this.menu.forEach((element, index) => {
        // We select al least the Dashboard in case no menu item selected
        if (index === 0) expandMenu(element)
        else collapseMenu(element)
        if (element.items) {
          const dashboardMenu = this.menu[0]
          setStatusToChild(element, payload, dashboardMenu)
        }
      })
    },
    resetMenuState() {
      this.menu = []
    },
  },
})
