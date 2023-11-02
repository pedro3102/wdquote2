// System Model
import { buildCreateRouteName, buildPath, buildUpdateRouteName } from '@/shared/config/router/router-utils'
import { ROLES } from '@/shared/const/auth-constants'
import { RouteRecordRaw } from 'vue-router'
import { systemModelRouteName } from '@/views/home/mod/inventory/system-model/system-model-entity'

const SystemModel = () => import(/* webpackChunkName: "group-admin-catalog" */ './SystemModel.vue')
const SystemModelUpdate = () => import(/* webpackChunkName: "group-admin-catalog" */ './SystemModelUpdate.vue')

const breadcrumbName = 'systemModel.home.title'
export const systemModelRoutes: RouteRecordRaw[] = [
  {
    path: buildPath(systemModelRouteName),
    name: systemModelRouteName,
    component: SystemModel,
    meta: {
      authorities: [ROLES.ROLE_ADMIN],
      lang: ['globalEntity', 'systemModel'],
      breadcrumb: breadcrumbName,
    },
    children: [
      {
        path: 'new',
        name: buildCreateRouteName(systemModelRouteName),
        component: SystemModelUpdate,
        meta: { authorities: [ROLES.ROLE_ADMIN] },
      },
      {
        path: ':id/edit',
        name: buildUpdateRouteName(systemModelRouteName),
        component: SystemModelUpdate,
        meta: { authorities: [ROLES.ROLE_ADMIN] },
      },
    ],
  },
]

export default systemModelRoutes
