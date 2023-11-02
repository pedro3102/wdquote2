import { buildPath } from '@/shared/config/router/router-utils'
import { dashboardRouteName } from '@/views/home/dashboard/routes'
import { RouteRecordRaw } from 'vue-router'

const Home = () => import(/* webpackChunkName: "Home" */ './Home.vue')

export const homeRouteName = 'home'

export const getHomePath = () => buildPath(homeRouteName)

const loadChildren = allRoutes => allRoutes.keys().reduce((previous, current) => previous.concat(allRoutes(current).default), [])

export const homeRoutes: RouteRecordRaw[] = [
  {
    path: buildPath(homeRouteName),
    name: homeRouteName,
    component: Home,
    redirect: { name: dashboardRouteName },
    meta: {
      breadcrumb: 'Home',
      breadcrumbIcon: 'home',
      authorities: ['ROLE_USER'],
      lang: ['global', 'error'],
    },
    children: loadChildren(require.context('.', true, /routes.(js|ts)$/)) /*loadChildren(import.meta.glob('./!*.ts'))*/,
  },
]

export default homeRoutes
