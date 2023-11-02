// DeliveryZone
import { buildCreateRouteName, buildPath, buildUpdateRouteName } from '@/shared/config/router/router-utils'
import { ROLES } from '@/shared/const/auth-constants'
import { RouteRecordRaw } from 'vue-router'
import { deliveryZoneRouteName } from '@/views/home/mod/inventory/delivery-zone/delivery-zone-entity'

const DeliveryZone = () => import(/* webpackChunkName: "group-admin-catalog" */ './DeliveryZone.vue')
const DeliveryZoneUpdate = () => import(/* webpackChunkName: "group-admin-catalog" */ './DeliveryZoneUpdate.vue')

const breadcrumbName = 'deliveryZone.home.title'
export const deliveryZoneRoutes: RouteRecordRaw[] = [
  {
    path: buildPath(deliveryZoneRouteName),
    name: deliveryZoneRouteName,
    component: DeliveryZone,
    meta: {
      authorities: [ROLES.ROLE_ADMIN],
      lang: ['globalEntity', 'deliveryZone'],
      breadcrumb: breadcrumbName,
    },
    children: [
      {
        path: 'new',
        name: buildCreateRouteName(deliveryZoneRouteName),
        component: DeliveryZoneUpdate,
        meta: { authorities: [ROLES.ROLE_ADMIN] },
      },
      {
        path: ':id/edit',
        name: buildUpdateRouteName(deliveryZoneRouteName),
        component: DeliveryZoneUpdate,
        meta: { authorities: [ROLES.ROLE_ADMIN] },
      },
    ],
  },
]

export default deliveryZoneRoutes
