import { buildCreateRouteName, buildPath, buildUpdateRouteName } from '@/shared/config/router/router-utils'
import { ROLES } from '@/shared/const/auth-constants'
import { RouteRecordRaw } from 'vue-router'
import { productCategoryRouteName } from '@/views/home/mod/inventory/product-category/product-category-entity'

const ProductCategory = () => import(/* webpackChunkName: "group-admin-catalog" */ './ProductCategory.vue')
const ProductCategoryUpdate = () => import(/* webpackChunkName: "group-admin-catalog" */ './ProductCategoryUpdate.vue')

const breadcrumbName = 'productCategory.home.title'
export const productCategoryRoutes: RouteRecordRaw[] = [
  {
    path: buildPath(productCategoryRouteName),
    name: productCategoryRouteName,
    component: ProductCategory,
    meta: {
      authorities: [ROLES.ROLE_ADMIN],
      lang: ['globalEntity', 'productCategory'],
      breadcrumb: breadcrumbName,
    },
    children: [
      {
        path: 'new',
        name: buildCreateRouteName(productCategoryRouteName),
        component: ProductCategoryUpdate,
        meta: { authorities: [ROLES.ROLE_ADMIN] },
      },
      {
        path: ':id/edit',
        name: buildUpdateRouteName(productCategoryRouteName),
        component: ProductCategoryUpdate,
        meta: { authorities: [ROLES.ROLE_ADMIN] },
      },
    ],
  },
]

export default productCategoryRoutes
