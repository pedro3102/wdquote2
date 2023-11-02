import {ROLES} from '@/shared/const/auth-constants'
import {RouteRecordRaw} from 'vue-router'
import {movementDetailRouteName} from '@/views/home/mod/inventory/movement-detail/movement-detail-entity'
import {buildCreateRouteName, buildUpdateRouteName} from '@/shared/config/router/router-utils'

const MovementDetailUpdate = () => import(/* webpackChunkName: "group-admin-catalog" */ './MovementDetailUpdate.vue')

export const movementDetailRoutes: RouteRecordRaw[] = [
  {
    path: ':id/detail/create',
    name: buildCreateRouteName(movementDetailRouteName),
    component: MovementDetailUpdate,
    meta: {authorities: [ROLES.ROLE_ADMIN]},
  },
  {
    path: ':id/detail/:detId/edit',
    name: buildUpdateRouteName(movementDetailRouteName),
    component: MovementDetailUpdate,
    meta: {authorities: [ROLES.ROLE_ADMIN]},
  },
]

export default movementDetailRoutes
