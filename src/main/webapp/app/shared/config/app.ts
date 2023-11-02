import { initAppRoute } from '@/shared/config/router/app-router'
import { App } from 'vue'
import { initPinia } from '@/shared/config/pinia'
import { initPrime } from '@/shared/config/prime'
import { eventBus } from '@/shared/config/event-bus'
import { initI18N } from '@/shared/config/i18n/i18n'
import TranslationService from '@/locale/translation-service'
import { initFortAwesome } from '@/shared/config/awesome'
import { accountServiceInstance } from '@/store/modules/account/account-service'
import initVue from '@/shared/config/directives'

const app = {
  init: (vueApp: App<Element>) => {
    vueApp.config.globalProperties.emitter = eventBus
    initVue(vueApp)
    initPrime(vueApp)
    initFortAwesome(vueApp)
    initPinia(vueApp)
    const i18n = initI18N(vueApp)
    const translationService = TranslationService.getInstance(i18n) // Keep this order because using Pinia
    const accountService = accountServiceInstance()
    vueApp.provide('translationService', translationService)
    vueApp.provide('accountService', accountService)
    initAppRoute(vueApp, translationService, accountService)
    // vueApp.provide('alertService', AlertService.getInstance())
  },
}

export default app
