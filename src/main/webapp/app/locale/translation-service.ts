import { mergeDeep } from '@/shared/util'
import { HttpClient } from '@/shared/api/http/http-client'
import { Composer } from 'vue-i18n'
import { useLangStore } from '@/store/modules/i18n/translation-store'

export default class TranslationService {
  private static instance: TranslationService
  private i18n: Composer
  private store

  /**
   * The Singleton's constructor should always be private to prevent direct
   * construction calls with the `new` operator.
   */
  private constructor(i18n: Composer) {
    this.store = useLangStore()
    this.store.setI18n(i18n)
  }

  /**
   * The static method that controls the access to the singleton instance.
   *
   * This implementation let you subclass the Singleton class while keeping
   * just one instance of each subclass around.
   */
  public static getInstanced(): TranslationService {
    return TranslationService.instance
  }

  /**
   * The static method that controls the access to the singleton instance.
   *
   * This implementation let you subclass the Singleton class while keeping
   * just one instance of each subclass around.
   */
  public static getInstance(i18n: any): TranslationService {
    if (!TranslationService.instance) {
      TranslationService.instance = new TranslationService(i18n)
    }
    return TranslationService.instance
  }

  /*  public async refreshTranslation(newLanguage: string) {
    if (this.i18n && !this.i18n.messages[newLanguage]) {
      const res = await axios.get(`i18n/${newLanguage}.json?_=${I18N_HASH}`);
      this.i18n.setLocaleMessage(newLanguage, res.data);
    }
  }
    public setLocale(lang: string) {
    dayjs.locale(lang);
    this.i18n.locale.value = lang;
    axios.defaults.headers.common['Accept-Language'] = lang;
    document.querySelector('html').setAttribute('lang', lang);
  }

  public isLanguageSupported(lang: string) {
    return Boolean(this.languages[lang]);
  }

  public getLocalStoreLanguage(): string | null {
    return localStorage.getItem('currentLanguage');
  }
  */

  public setI18nLanguage(currentLanguage: string): void {
    this.store.i18n.locale = currentLanguage
    //  HttpClient.defaults.headers.common['Accept-Language'] = currentLanguage
    document.querySelector('html')?.setAttribute('lang', currentLanguage)
  }

  public loadLanguageAsync(files: string[] = []): void {
    const common = ['global', 'mainMenu', 'header', 'error']
    const refresh = this.store.i18n.global.locale !== this.store.getCurrentLanguage
    common.forEach((item: string) => {
      // Always global loaded
      if (!files.includes(item)) {
        files.push(item)
      }
    })

    files.forEach((file: string) => {
      if (!this.store?.getFilesLoaded?.includes(`${this.store.getCurrentLanguage}/${file}`) || refresh) {
        import(`../../i18n/${this.store.getCurrentLanguage}/${file}.json`).then(messages => {
          this.store.i18n.global.locale = this.store.getCurrentLanguage
          const current = this.store.i18n.global.getLocaleMessage(this.store.getCurrentLanguage)
          this.store.i18n.global.setLocaleMessage(this.store.getCurrentLanguage, mergeDeep(current, messages.default))
          this.store.setFilesLoaded(`${this.store.getCurrentLanguage}/${file}`)
          return this.setI18nLanguage(this.store.getCurrentLanguage)
        })
      }
    })
  }

  /**
   * This method allow to call the translation service instance method directly.
   * @param key String The string key to translate.
   * @param params The params for translation.
   * @returns { String } The translated value for the key.
   */
  // eslint-disable-next-line id-length
  public t(key: string, params?: object): string {
    return this.store.t(key, params)
  }
}
