import { App } from 'vue'

// PrimeVue
import 'primevue/resources/themes/lara-light-indigo/theme.css'
import '../../../content/scss/styles.scss'

import PrimeVue from 'primevue/config'
import ToastService from 'primevue/toastservice'
import ConfirmationService from 'primevue/confirmationservice'
import Tooltip from 'primevue/tooltip'

export const initPrime = (vueApp: App<Element>): void => {
  vueApp.use(PrimeVue)
  vueApp.use(ToastService)
  vueApp.use(ConfirmationService)
  vueApp.directive('tooltip', Tooltip)
}
