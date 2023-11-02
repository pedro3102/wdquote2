// Stock Position
import {buildCreateRouteName, buildPath, buildUpdateRouteName} from '@/shared/config/router/router-utils'
import {ROLES} from '@/shared/const/auth-constants'
import {RouteRecordRaw} from 'vue-router'
import {stockPositionRouteName} from '@/views/home/mod/inventory/stock-position/stock-position-entity'

const StockPosition = () => import(/* webpackChunkName: "group-admin-catalog" */ './StockPosition.vue')
const StockPositionUpdate = () => import(/* webpackChunkName: "group-admin-catalog" */ './StockPositionUpdate.vue')

const breadcrumbName = 'stockPosition.home.title'
export const stockPositionRoutes: RouteRecordRaw[] = [
  {
    path: buildPath(stockPositionRouteName),
    name: stockPositionRouteName,
    component: StockPosition,
    meta: {
      authorities: [ROLES.ROLE_ADMIN],
      lang: ['globalEntity', 'stockPosition'],
      breadcrumb: breadcrumbName,
    },
    children: [
      {
        path: 'new',
        name: buildCreateRouteName(stockPositionRouteName),
        component: StockPositionUpdate,
        meta: {authorities: [ROLES.ROLE_ADMIN]},
      },
      {
        path: ':id/edit',
        name: buildUpdateRouteName(stockPositionRouteName),
        component: StockPositionUpdate,
        meta: {authorities: [ROLES.ROLE_ADMIN]},
      },
    ],
  },
]

export default stockPositionRoutes
