import { createPinia } from 'pinia';
import { App } from 'vue';

const pinia = createPinia();

export const initPinia = (vueApp: App<Element>): void => {
  vueApp.use(createPinia());
};

export default pinia;
