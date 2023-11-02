//Customer
import { buildCreateRouteName, buildPath, buildUpdateRouteName } from '@/shared/config/router/router-utils'
import { ROLES } from '@/shared/const/auth-constants'
import { RouteRecordRaw } from 'vue-router'
import { customerRouteName } from '@/views/home/mod/inventory/customer/customer-entity'

const Customer = () => import(/* webpackChunkName: "group-admin-catalog" */ './Customer.vue')
const CustomerUpdate = () => import(/* webpackChunkName: "group-admin-catalog" */ './CustomerUpdate.vue')

const breadcrumbName = 'customer.home.title'
export const customerRoutes: RouteRecordRaw[] = [
  {
    path: buildPath(customerRouteName),
    name: customerRouteName,
    component: Customer,
    meta: {
      authorities: [ROLES.ROLE_ADMIN],
      lang: ['globalEntity', 'customer', 'customerContact'],
      breadcrumb: breadcrumbName,
    },
    children: [
      {
        path: 'new',
        name: buildCreateRouteName(customerRouteName),
        component: CustomerUpdate,
        meta: { authorities: [ROLES.ROLE_ADMIN] },
      },
      {
        path: ':id/edit',
        name: buildUpdateRouteName(customerRouteName),
        component: CustomerUpdate,
        meta: { authorities: [ROLES.ROLE_ADMIN] },
      },
    ],
  },
]

export default customerRoutes
