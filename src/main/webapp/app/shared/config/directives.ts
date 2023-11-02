import { App } from 'vue'
import directives from '../directives'

export const initVue = (vueApp: App<Element>): void => {
  Object.keys(directives).forEach((directive: string) => {
    vueApp.directive(directive, directives[directive])
  })
}

export default initVue
