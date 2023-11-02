import {IMenuItem} from '@/store/modules/menu/menu-store-model'
import {useMenuStore} from '@/store/modules/menu/menu-store'
import {buildPath} from '@/shared/config/router/router-utils'
import {ACTIONS} from '@/shared/const/auth-constants'
import {productRouteName} from '@/views/home/mod/inventory/product/product-entity'
import {movementTypeRouteName} from '@/views/home/mod/inventory/movement-type/movement-type-entity'
import {movementRouteName} from '@/views/home/mod/inventory/movement/movement-entity'
import {inventoryRouteName} from '@/views/home/mod/inventory/inventory/inventory-entity'
import {stockPositionRouteName} from "@/views/home/mod/inventory/stock-position/stock-position-entity";

const menuStore = useMenuStore()

const adminMenu = (): IMenuItem =>
  // Inventory
  ({
    id: 3, // 'admin',
    open: false,
    label: 'mainMenu.inventory.menu',
    icon: 'cogs',
    to: '',
    access: menuStore.getMenuAccess(ACTIONS.ACTION_MAIN_MENU_INVENTORY),
    items: [
      {
        id: productRouteName,
        label: 'mainMenu.inventory.products',
        to: buildPath(productRouteName),
        icon: 'boxes-stacked',
        access: menuStore.getMenuAccess(ACTIONS.ACTION_MAIN_MENU_INVENTORY_PRODUCT),
      },
      {
        id: movementTypeRouteName,
        label: 'mainMenu.inventory.movementTypes',
        to: buildPath(movementTypeRouteName),
        icon: 'retweet',
        access: menuStore.getMenuAccess(ACTIONS.ACTION_MAIN_MENU_INVENTORY_MOVEMENT_TYPE),
      },
      {
        id: stockPositionRouteName,
        label: 'mainMenu.inventory.stockPosition',
        to: buildPath(stockPositionRouteName),
        icon: 'thumbtack',
        access: menuStore.getMenuAccess(ACTIONS.ACTION_MAIN_MENU_INVENTORY_STOCK_POSITION),
      },
      {
        id: inventoryRouteName,
        label: 'mainMenu.inventory.inventory',
        to: buildPath(inventoryRouteName),
        icon: 'warehouse',
        access: menuStore.getMenuAccess(ACTIONS.ACTION_MAIN_MENU_INVENTORY_ITEMS),
      },
      {
        id: movementRouteName,
        label: 'mainMenu.inventory.movements',
        to: buildPath(movementRouteName),
        icon: 'truck-fast',
        access: menuStore.getMenuAccess(ACTIONS.ACTION_MAIN_MENU_INVENTORY_MOVEMENT),
      },
    ],
  })

export default adminMenu
