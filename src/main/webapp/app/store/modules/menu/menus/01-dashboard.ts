import { dashBoardRouteName } from '@/views/home/dashboard/dashboard-entity'
import { ACTIONS } from '@/shared/const/auth-constants'
import { buildPath } from '@/shared/config/router/router-utils'
import { IMenuItem } from '@/store/modules/menu/menu-store-model'
import { useMenuStore } from '@/store/modules/menu/menu-store'

const menuStore = useMenuStore()

const dashboardMenu = (): IMenuItem =>
  // Dashboard
  ({
    id: 1, // 'home',
    open: false,
    label: 'mainMenu.home.menu',
    access: menuStore.getMenuAccess(ACTIONS.ACTION_MAIN_MENU_DASHBOARD),
    items: [
      {
        open: true,
        label: 'mainMenu.dashboard.menu',
        icon: 'tachometer-alt',
        to: buildPath(dashBoardRouteName),
        access: menuStore.getMenuAccess(ACTIONS.ACTION_MAIN_MENU_DASHBOARD),
        items: [],
      },
    ],
  })

export default dashboardMenu
