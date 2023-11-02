import {buildCreateRouteName, buildPath, buildUpdateRouteName} from '@/shared/config/router/router-utils'
import {ROLES} from '@/shared/const/auth-constants'
import {RouteRecordRaw} from 'vue-router'
import {movementRouteName} from '@/views/home/mod/inventory/movement/movement-entity'
import movementDetailRoutes from '@/views/home/mod/inventory/movement-detail/movement-detail-routes'

const Movement = () => import(/* webpackChunkName: "group-admin-catalog" */ './Movement.vue')
const MovementUpdate = () => import(/* webpackChunkName: "group-admin-catalog" */ './MovementUpdate.vue')

const breadcrumbName = 'movement.home.title'
export const movementRoutes: RouteRecordRaw[] = [
  {
    path: buildPath(movementRouteName),
    name: movementRouteName,
    component: Movement,
    meta: {
      authorities: [ROLES.ROLE_ADMIN],
      lang: ['globalEntity', 'movement', 'movementDetail', 'product'],
      breadcrumb: breadcrumbName,
    },
    children: [
      {
        path: 'new',
        name: buildCreateRouteName(movementRouteName),
        component: MovementUpdate,
        meta: {authorities: [ROLES.ROLE_ADMIN]},
      },
      {
        path: ':id/edit',
        name: buildUpdateRouteName(movementRouteName),
        component: MovementUpdate,
        meta: {authorities: [ROLES.ROLE_ADMIN]},
      },
      ...movementDetailRoutes,
    ],
  },
]

export default movementRoutes
