import { ROLES } from '@/shared/const/auth-constants';

export interface IMeta {
  authorities: ROLES[];
  breadcrumb?: string;
  lang?: string[];
}
