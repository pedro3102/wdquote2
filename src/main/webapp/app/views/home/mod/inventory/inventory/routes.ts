import { buildPath } from '@/shared/config/router/router-utils'
import { ROLES } from '@/shared/const/auth-constants'
import { RouteRecordRaw } from 'vue-router'
import { inventoryRouteName } from '@/views/home/mod/inventory/inventory/inventory-entity'

const Inventory = () => import(/* webpackChunkName: "group-admin-catalog" */ './Inventory.vue')

const breadcrumbName = 'inventory.home.title'
export const inventoryRoutes: RouteRecordRaw[] = [
  {
    path: buildPath(inventoryRouteName),
    name: inventoryRouteName,
    component: Inventory,
    meta: {
      authorities: [ROLES.ROLE_ADMIN],
      lang: ['globalEntity', 'inventory', 'product'],
      breadcrumb: breadcrumbName,
    },
    children: [],
  },
]

export default inventoryRoutes
