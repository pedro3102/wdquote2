// TaxAreaCode
import { buildCreateRouteName, buildPath, buildUpdateRouteName } from '@/shared/config/router/router-utils'
import { ROLES } from '@/shared/const/auth-constants'
import { RouteRecordRaw } from 'vue-router'
import { taxAreaCodeRouteName } from '@/views/home/mod/inventory/tax-area-code/tax-area-code-entity'

const TaxAreaCode = () => import(/* webpackChunkName: "group-admin-catalog" */ './TaxAreaCode.vue')
const TaxAreaCodeUpdate = () => import(/* webpackChunkName: "group-admin-catalog" */ './TaxAreaCodeUpdate.vue')

const breadcrumbName = 'taxAreaCode.home.title'
export const taxAreaCodeRoutes: RouteRecordRaw[] = [
  {
    path: buildPath(taxAreaCodeRouteName),
    name: taxAreaCodeRouteName,
    component: TaxAreaCode,
    meta: {
      authorities: [ROLES.ROLE_ADMIN],
      lang: ['globalEntity', 'taxAreaCode'],
      breadcrumb: breadcrumbName,
    },
    children: [
      {
        path: 'new',
        name: buildCreateRouteName(taxAreaCodeRouteName),
        component: TaxAreaCodeUpdate,
        meta: { authorities: [ROLES.ROLE_ADMIN] },
      },
      {
        path: ':id/edit',
        name: buildUpdateRouteName(taxAreaCodeRouteName),
        component: TaxAreaCodeUpdate,
        meta: { authorities: [ROLES.ROLE_ADMIN] },
      },
    ],
  },
]

export default taxAreaCodeRoutes
