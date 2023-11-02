// Users
import { buildCreateRouteName, buildPath, buildUpdateRouteName } from '@/shared/config/router/router-utils'
import { ROLES } from '@/shared/const/auth-constants'
import { RouteRecordRaw } from 'vue-router'
import { userRouteName } from './user-management-entity'

const UserManagement = () => import(/* webpackChunkName: "group-admin-catalog" */ './UserManagement.vue')
const UserManagementUpdate = () => import(/* webpackChunkName: "group-admin-catalog" */ './UserManagementUpdate.vue')

const breadcrumbName = 'user.home.title'

export const userRoutes: RouteRecordRaw[] = [
  {
    path: buildPath(userRouteName),

    name: userRouteName,
    component: UserManagement,
    meta: {
      authorities: [ROLES.ROLE_ADMIN],
      lang: ['globalEntity', 'user'],
      breadcrumb: breadcrumbName,
    },
    children: [
      {
        path: 'new',
        name: buildCreateRouteName(userRouteName),
        component: UserManagementUpdate,
        meta: { authorities: [ROLES.ROLE_ADMIN] },
      },
      {
        path: ':login/edit',
        name: buildUpdateRouteName(userRouteName),
        component: UserManagementUpdate,
        meta: { authorities: [ROLES.ROLE_ADMIN] },
      },
    ],
  },
]

export default userRoutes
