import { IMenuItem } from '@/store/modules/menu/menu-store-model'
import { useMenuStore } from '@/store/modules/menu/menu-store'
import { userRouteName } from '@/views/home/admin/user-management/user-management-entity'
import { buildPath } from '@/shared/config/router/router-utils'
import { ACTIONS } from '@/shared/const/auth-constants'

const menuStore = useMenuStore()

const adminMenu = (): IMenuItem =>
  // Administration
  ({
    id: 4, // 'admin',
    open: false,
    label: 'mainMenu.admin.menu',
    icon: 'cogs',
    to: '',
    access: menuStore.getMenuAccess(ACTIONS.ACTION_MAIN_MENU_ADMIN),
    items: [
      {
        id: userRouteName,
        label: 'mainMenu.admin.user',
        to: buildPath(userRouteName),
        icon: 'users',
        access: menuStore.getMenuAccess(ACTIONS.ACTION_MAIN_MENU_ADMIN),
      },
    ],
  })

export default adminMenu
