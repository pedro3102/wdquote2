// UnitOfMeasureConversion
import { ROLES } from '@/shared/const/auth-constants'
import { RouteRecordRaw } from 'vue-router'
import { unitOfMeasureConversionRouteName } from '@/views/home/mod/inventory/unit-of-measure-conversion/unit-of-measure-conversion-entity'
import { buildCreateRouteName, buildUpdateRouteName } from '@/shared/config/router/router-utils'

const UnitOfMeasureConversionUpdate = () => import(/* webpackChunkName: "group-admin-catalog" */ './UnitOfMeasureConversionUpdate.vue')

export const unitOfMeasureConversionRoutes: RouteRecordRaw[] = [
  {
    path: ':id/conversion/create',
    name: buildCreateRouteName(unitOfMeasureConversionRouteName),
    component: UnitOfMeasureConversionUpdate,
    meta: { authorities: [ROLES.ROLE_ADMIN] },
  },
  {
    path: ':id/conversion/:convId/edit',
    name: buildUpdateRouteName(unitOfMeasureConversionRouteName),
    component: UnitOfMeasureConversionUpdate,
    meta: { authorities: [ROLES.ROLE_ADMIN] },
  },
]

export default unitOfMeasureConversionRoutes
