// Locations
import { buildCreateRouteName, buildPath, buildUpdateRouteName } from '@/shared/config/router/router-utils'
import { ROLES } from '@/shared/const/auth-constants'
import { RouteRecordRaw } from 'vue-router'
import { locationRouteName } from '@/views/home/mod/inventory/location/location-entity'

const Location = () => import(/* webpackChunkName: "group-admin-catalog" */ './Location.vue')
const LocationUpdate = () => import(/* webpackChunkName: "group-admin-catalog" */ './LocationUpdate.vue')

const breadcrumbName = 'location.home.title'
export const locationRoutes: RouteRecordRaw[] = [
  {
    path: buildPath(locationRouteName),
    name: locationRouteName,
    component: Location,
    meta: {
      authorities: [ROLES.ROLE_ADMIN],
      lang: ['globalEntity', 'location', 'user', 'vendor'],
      breadcrumb: breadcrumbName,
    },
    children: [
      {
        path: 'new',
        name: buildCreateRouteName(locationRouteName),
        component: LocationUpdate,
        meta: { authorities: [ROLES.ROLE_ADMIN] },
      },
      {
        path: ':id/edit',
        name: buildUpdateRouteName(locationRouteName),
        component: LocationUpdate,
        meta: { authorities: [ROLES.ROLE_ADMIN] },
      },
    ],
  },
]

export default locationRoutes
