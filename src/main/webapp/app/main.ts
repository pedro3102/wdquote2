import { App, createApp } from 'vue'
import app from '@/shared/config/app'
import Main from './App.vue'

const vueApp: App<Element> = createApp(Main)
app.init(vueApp)
vueApp.mount('#app')

export default vueApp
