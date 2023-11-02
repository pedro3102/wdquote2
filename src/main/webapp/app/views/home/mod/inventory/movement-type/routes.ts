import { buildCreateRouteName, buildPath, buildUpdateRouteName } from '@/shared/config/router/router-utils'
import { ROLES } from '@/shared/const/auth-constants'
import { RouteRecordRaw } from 'vue-router'
import { movementTypeRouteName } from '@/views/home/mod/inventory/movement-type/movement-type-entity'

const Language = () => import(/* webpackChunkName: "group-admin-catalog" */ './MovementType.vue')
const LanguageUpdate = () => import(/* webpackChunkName: "group-admin-catalog" */ './MovementTypeUpdate.vue')

const breadcrumbName = 'movementType.home.title'
export const movementTypeRoutes: RouteRecordRaw[] = [
  {
    path: buildPath(movementTypeRouteName),
    name: movementTypeRouteName,
    component: Language,
    meta: {
      authorities: [ROLES.ROLE_ADMIN],
      lang: ['globalEntity', 'movementType'],
      breadcrumb: breadcrumbName,
    },
    children: [
      {
        path: 'new',
        name: buildCreateRouteName(movementTypeRouteName),
        component: LanguageUpdate,
        meta: { authorities: [ROLES.ROLE_ADMIN] },
      },
      {
        path: ':id/edit',
        name: buildUpdateRouteName(movementTypeRouteName),
        component: LanguageUpdate,
        meta: { authorities: [ROLES.ROLE_ADMIN] },
      },
    ],
  },
]

export default movementTypeRoutes
