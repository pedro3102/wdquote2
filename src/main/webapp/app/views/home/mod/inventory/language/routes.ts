import { buildCreateRouteName, buildPath, buildUpdateRouteName } from '@/shared/config/router/router-utils'
import { ROLES } from '@/shared/const/auth-constants'
import { RouteRecordRaw } from 'vue-router'
import { languageRouteName } from '@/views/home/mod/inventory/language/language-entity'

const Language = () => import(/* webpackChunkName: "group-admin-catalog" */ './Language.vue')
const LanguageUpdate = () => import(/* webpackChunkName: "group-admin-catalog" */ './LanguageUpdate.vue')

const breadcrumbName = 'language.home.title'
export const languageRoutes: RouteRecordRaw[] = [
  {
    path: buildPath(languageRouteName),
    name: languageRouteName,
    component: Language,
    meta: {
      authorities: [ROLES.ROLE_ADMIN],
      lang: ['globalEntity', 'language'],
      breadcrumb: breadcrumbName,
    },
    children: [
      {
        path: 'new',
        name: buildCreateRouteName(languageRouteName),
        component: LanguageUpdate,
        meta: { authorities: [ROLES.ROLE_ADMIN] },
      },
      {
        path: ':id/edit',
        name: buildUpdateRouteName(languageRouteName),
        component: LanguageUpdate,
        meta: { authorities: [ROLES.ROLE_ADMIN] },
      },
    ],
  },
]

export default languageRoutes
