import { ROLES } from '@/shared/const/auth-constants';
import { RouteRecordRaw } from 'vue-router';
import Forbidden from './Forbidden.vue';
import { buildPath } from '@/shared/config/router/router-utils';

export const forbiddenRouteName = 'forbidden';

export const getForbiddenPath = () => buildPath(forbiddenRouteName);

export const forbiddenRoutes: RouteRecordRaw[] = [
  {
    path: buildPath(forbiddenRouteName),
    name: forbiddenRouteName,
    component: Forbidden,
    meta: {
      authorities: [ROLES.ROLE_USER],
      lang: [],
      breadcrumb: 'error.forbidden.breadcrumb',
    },
  },
];

export default forbiddenRoutes;
