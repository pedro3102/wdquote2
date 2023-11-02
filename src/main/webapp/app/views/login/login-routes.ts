import { RouteRecordRaw } from 'vue-router';
import { buildPath } from '@/shared/config/router/router-utils';
import { loginRouteName } from '@/views/login/route-names';

const Login = () => import('./Login.vue');

export const loginRoutes: RouteRecordRaw[] = [
  {
    path: buildPath(loginRouteName),
    name: loginRouteName,
    component: Login,
    meta: {
      lang: ['login', 'error'],
    },
  },
];

export default loginRoutes;
