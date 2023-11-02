//Product
import { buildCreateRouteName, buildPath, buildUpdateRouteName } from '@/shared/config/router/router-utils'
import { ROLES } from '@/shared/const/auth-constants'
import { RouteRecordRaw } from 'vue-router'
import { productRouteName } from '@/views/home/mod/inventory/product/product-entity'

const Product = () => import(/* webpackChunkName: "group-admin-catalog" */ './Product.vue')
const ProductUpdate = () => import(/* webpackChunkName: "group-admin-catalog" */ './ProductUpdate.vue')

const breadcrumbName = 'product.home.title'
export const productRoutes: RouteRecordRaw[] = [
  {
    path: buildPath(productRouteName),
    name: productRouteName,
    component: Product,
    meta: {
      authorities: [ROLES.ROLE_ADMIN],
      lang: ['globalEntity', 'product'],
      breadcrumb: breadcrumbName,
    },
    children: [
      {
        path: 'new',
        name: buildCreateRouteName(productRouteName),
        component: ProductUpdate,
        meta: { authorities: [ROLES.ROLE_ADMIN] },
      },
      {
        path: ':id/edit',
        name: buildUpdateRouteName(productRouteName),
        component: ProductUpdate,
        meta: { authorities: [ROLES.ROLE_ADMIN] },
      },
    ],
  },
]

export default productRoutes
