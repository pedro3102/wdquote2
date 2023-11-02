import { buildCreateRouteName, buildPath, buildUpdateRouteName } from '@/shared/config/router/router-utils'
import { ROLES } from '@/shared/const/auth-constants'
import { RouteRecordRaw } from 'vue-router'
import { systemRouteName } from '@/views/home/mod/inventory/system/system-entity'

const System = () => import(/* webpackChunkName: "group-admin-catalog" */ './System.vue')
const SystemUpdate = () => import(/* webpackChunkName: "group-admin-catalog" */ './SystemUpdate.vue')

const breadcrumbName = 'system.home.title'
export const systemRoutes: RouteRecordRaw[] = [
  {
    path: buildPath(systemRouteName),
    name: systemRouteName,
    component: System,
    meta: {
      authorities: [ROLES.ROLE_ADMIN],
      lang: ['globalEntity', 'system'],
      breadcrumb: breadcrumbName,
    },
    children: [
      {
        path: 'new',
        name: buildCreateRouteName(systemRouteName),
        component: SystemUpdate,
        meta: { authorities: [ROLES.ROLE_ADMIN] },
      },
      {
        path: ':id/edit',
        name: buildUpdateRouteName(systemRouteName),
        component: SystemUpdate,
        meta: { authorities: [ROLES.ROLE_ADMIN] },
      },
    ],
  },
]

export default systemRoutes
