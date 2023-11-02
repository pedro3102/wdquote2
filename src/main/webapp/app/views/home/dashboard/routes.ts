// Dashboard
import { buildPath } from '@/shared/config/router/router-utils';
import { RouteRecordRaw } from 'vue-router';
import { dashBoardRouteName } from './dashboard-entity';

const Dashboard = () => import(/* webpackChunkName: "group-dashboard" */ './Dashboard.vue');

export const dashboardRouteName = 'dashboard';

export const getDashboardPath = () => buildPath(dashboardRouteName);

export const dashboardRoutes: RouteRecordRaw[] = [
  {
    path: buildPath(dashBoardRouteName),
    name: dashboardRouteName,
    component: Dashboard,
    meta: {
      authorities: ['ROLE_USER'],
      lang: ['dashboard', 'user'],
    },
  },
];

export default dashboardRoutes;
