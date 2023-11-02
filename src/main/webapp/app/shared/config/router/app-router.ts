import { createRouter, createWebHistory, NavigationGuardNext, RouteLocationNormalized, Router, RouteRecordRaw } from 'vue-router'
import { homeRoutes } from '@/views/home/home-route'
import { loginRoutes } from '@/views/login/login-routes'
import TranslationService from '@/locale/translation-service'
import { App } from 'vue'
import { getNotFoundPath } from '@/views/home/not-found/routes'
import { configInterceptors } from '@/shared/api/http/http-client'
import { getLoginPath } from '@/views/login/route-names'
import { AccountService } from '@/store/modules/account/account-service'
import { useAccountStore } from '@/store/modules/account/account-store'
import { IErrorDetails } from '@/shared/error/response-detail-model'
import { getDetails } from '@/shared/error/response'
import { getForbiddenPath } from '@/views/home/forbidden/routes'

const routes: RouteRecordRaw[] = []

loginRoutes.forEach((route: RouteRecordRaw) => {
  routes.push(route)
})
homeRoutes.forEach((route: RouteRecordRaw) => {
  routes.push(route)
})

if (process.env.NODE_ENV === 'development') console.log(routes)

const loadLanguage = (to: RouteLocationNormalized, translationService) => {
  to.matched.forEach(match => {
    const { lang } = match.meta
    translationService.loadLanguageAsync((lang as string[])?.length ? lang : [])
  })
}

/*const selectMenuItem = (to: RouteLocationNormalized): void => {
  const menuStore = useMenuStore()
  menuStore.selectMenuItem({ itemId: to.name, matched: to.matched })
}*/

/*const setRequestedUrl = (to: RouteLocationNormalized): void => {
  const toExclude: string[] = [loginRouteName, twoFactorRouteName, twoFactorAppRouteName, twoFactorEmailRouteName,
    resetPasswordRouteName, recoveryPasswordRouteName, homeRouteName, dashboardRouteName, forbiddenRouteName]
  if (toExclude.indexOf(to.name as string) === -1) {
    sessionStorageInstance.setItem(storageRequestedUrlKey, to.fullPath)
  }
}*/

export const appRouter: Router = createRouter({
  history: createWebHistory(),
  routes,
})

export const initAppRoute = (vueApp: App<Element>, translationService: TranslationService, accountService: AccountService): void => {
  // Edit this method carefully because it handles all routes authentication and authorization
  appRouter.beforeEach((to: RouteLocationNormalized, from: RouteLocationNormalized, next: NavigationGuardNext) => {
    if (process.env.NODE_ENV === 'development') console.log({ to, from }) // Enable debug mode in development
    loadLanguage(to, translationService)
    //selectMenuItem(to)
    //setRequestedUrl(to)
    if (!to.matched.length) {
      next(getNotFoundPath())
    } else if (to.meta && to.meta.authorities && (to.meta.authorities as string[]).length > 0) {
      accountService.hasAnyAuthorityAndCheckAuth(to.meta.authorities).then(value => {
        if (!value) {
          if (!accountService.isAuthenticated()) {
            next(getLoginPath())
          } else {
            next(getForbiddenPath())
          }
        } else {
          next()
        }
      })
    } else {
      // no authorities, so just proceed
      next()
    }
  })

  configInterceptors(
    async (error: any) => {
      const status = error.status ? error.status : error.request?.status
      const details: IErrorDetails = getDetails(error.response.data.detail)
      if (status === 401) {
        // Store logged out state.
        await accountService.logoutAccount()
        await appRouter.push(getLoginPath())
      }
      // console.log('Unauthorized!')
      return Promise.reject(error)
    },
    error => Promise.reject(error)
  ) // console.log('Server error!')

  vueApp.use(appRouter)
}

export default appRouter
