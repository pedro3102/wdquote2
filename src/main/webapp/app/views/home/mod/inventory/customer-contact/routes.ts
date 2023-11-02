//CustomerContact
import { buildCreateRouteName, buildPath, buildUpdateRouteName } from '@/shared/config/router/router-utils'
import { ROLES } from '@/shared/const/auth-constants'
import { RouteRecordRaw } from 'vue-router'
import { customerContactRouteName } from '@/views/home/mod/inventory/customer-contact/customer-contact-entity'

const CustomerContact = () => import(/* webpackChunkName: "group-admin-catalog" */ './CustomerContact.vue')
const CustomerContactUpdate = () => import(/* webpackChunkName: "group-admin-catalog" */ './CustomerContactUpdate.vue')

const breadcrumbName = 'customerContact.home.title'
export const customerContactRoutes: RouteRecordRaw[] = [
  {
    path: buildPath(customerContactRouteName),
    name: customerContactRouteName,
    component: CustomerContact,
    meta: {
      authorities: [ROLES.ROLE_ADMIN],
      lang: ['globalEntity', 'customerContact'],
      breadcrumb: breadcrumbName,
    },
    children: [
      {
        path: 'new',
        name: buildCreateRouteName(customerContactRouteName),
        component: CustomerContactUpdate,
        meta: { authorities: [ROLES.ROLE_ADMIN] },
      },
      {
        path: ':id/edit',
        name: buildUpdateRouteName(customerContactRouteName),
        component: CustomerContactUpdate,
        meta: { authorities: [ROLES.ROLE_ADMIN] },
      },
    ],
  },
]

export default customerContactRoutes
