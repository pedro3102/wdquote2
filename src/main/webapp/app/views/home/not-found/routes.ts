import { ROLES } from '@/shared/const/auth-constants';
import { RouteRecordRaw } from 'vue-router';
import NotFound from './NotFound.vue';

const notFoundRouteName = 'not-found';

const notFoundRoutePath = '/:pathMatch(.*)*';

export const getNotFoundPath = () => notFoundRoutePath;

export const notFoundRoutes: RouteRecordRaw[] = [
  {
    path: notFoundRoutePath,
    name: notFoundRouteName,
    component: NotFound,
    meta: {
      authorities: [ROLES.ROLE_USER],
      lang: [],
      breadcrumb: 'error.notFound.breadcrumb',
    },
  },
];

export default notFoundRoutes;
