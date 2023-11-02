import { createI18n, I18nOptions } from 'vue-i18n';
import { App } from 'vue';

const vueI18nObject: I18nOptions = {
  legacy: false,
  allowComposition: false,
  silentTranslationWarn: true,
  // locale: process.env.VUE_APP_I18N_LOCALE || 'en',
  // fallbackLocale: process.env.VUE_APP_I18N_FALLBACK_LOCALE || 'en',
  locale: 'en',
  fallbackLocale: 'en',
  messages: {
    en: {},
    es: {},
  },
  numberFormats: {
    en: {
      currency: {
        style: 'currency',
        currency: 'USD',
      },
    },
  },
};

const i18n: any = createI18n(vueI18nObject);

export const initI18N = (vueApp: App<Element>): any => {
  vueApp.use(i18n);
  return i18n;
};

export default i18n;
