import { buildCreateRouteName, buildPath, buildUpdateRouteName } from '@/shared/config/router/router-utils'
import { ROLES } from '@/shared/const/auth-constants'
import { RouteRecordRaw } from 'vue-router'
import { unitOfMeasureRouteName } from '@/views/home/mod/inventory/unit-of-measure/unit-of-measure-entity'
import unitOfMeasureConversionRoutes from '@/views/home/mod/inventory/unit-of-measure-conversion/unit-of-measure-conversion-routes'

const UnitOfMeasure = () => import(/* webpackChunkName: "group-admin-catalog" */ './UnitOfMeasure.vue')
const UnitOfMeasureUpdate = () => import(/* webpackChunkName: "group-admin-catalog" */ './UnitOfMeasureUpdate.vue')

const breadcrumbName = 'unitOfMeasure.home.title'
export const unitOfMeasureRoutes: RouteRecordRaw[] = [
  {
    path: buildPath(unitOfMeasureRouteName),
    name: unitOfMeasureRouteName,
    component: UnitOfMeasure,
    meta: {
      authorities: [ROLES.ROLE_ADMIN],
      lang: ['globalEntity', 'unitOfMeasure', 'unitOfMeasureConversion'],
      breadcrumb: breadcrumbName,
    },
    children: [
      {
        path: 'new',
        name: buildCreateRouteName(unitOfMeasureRouteName),
        component: UnitOfMeasureUpdate,
        meta: { authorities: [ROLES.ROLE_ADMIN] },
      },
      {
        path: ':id/edit',
        name: buildUpdateRouteName(unitOfMeasureRouteName),
        component: UnitOfMeasureUpdate,
        meta: { authorities: [ROLES.ROLE_ADMIN] },
      },
      ...unitOfMeasureConversionRoutes,
    ],
  },
]

export default unitOfMeasureRoutes
