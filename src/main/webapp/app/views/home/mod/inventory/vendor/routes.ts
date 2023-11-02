// Vendors
import { buildCreateRouteName, buildPath, buildUpdateRouteName } from '@/shared/config/router/router-utils'
import { ROLES } from '@/shared/const/auth-constants'
import { RouteRecordRaw } from 'vue-router'
import { vendorRouteName } from '@/views/home/mod/inventory/vendor/vendor-entity'

const Vendor = () => import(/* webpackChunkName: "group-admin-catalog" */ './Vendor.vue')
const VendorUpdate = () => import(/* webpackChunkName: "group-admin-catalog" */ './VendorUpdate.vue')

const breadcrumbName = 'vendor.home.title'
export const vendorRoutes: RouteRecordRaw[] = [
  {
    path: buildPath(vendorRouteName),
    name: vendorRouteName,
    component: Vendor,
    meta: {
      authorities: [ROLES.ROLE_ADMIN],
      lang: ['globalEntity', 'vendor'],
      breadcrumb: breadcrumbName,
    },
    children: [
      {
        path: 'new',
        name: buildCreateRouteName(vendorRouteName),
        component: VendorUpdate,
        meta: { authorities: [ROLES.ROLE_ADMIN] },
      },
      {
        path: ':id/edit',
        name: buildUpdateRouteName(vendorRouteName),
        component: VendorUpdate,
        meta: { authorities: [ROLES.ROLE_ADMIN] },
      },
    ],
  },
]

export default vendorRoutes
