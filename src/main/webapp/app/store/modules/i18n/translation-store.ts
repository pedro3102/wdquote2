import { defineStore } from 'pinia'
import { ILanguage, ILanguagesConfig } from '@/store/modules/i18n/lang-model'
import languages from '@/store/modules/i18n/lang-constants'
import { storageCurrentLanguageKey } from '@/shared/services/storage/storage-constants'
import { localStorageInstance } from '@/shared/services/storage/local-storage-service'

export interface ILangState {
  i18n?: any
  language: string
  languages: ILanguagesConfig
  files: string[]
}

export const initialLangState: ILangState = {
  language: localStorageInstance.getItem(storageCurrentLanguageKey) || 'en',
  languages,
  files: [],
}

export const useLangStore = defineStore({
  id: 'lang',
  state: () => initialLangState,
  getters: {
    getTranslate: (state): object => state.i18n.global,
    getCurrentLanguage: (state): string => state.language,
    getLanguages: (state): ILanguage[] => state.languages,
    getLanguagesKeys: (state): string[] => {
      const langKeys: string[] = []
      Object.values(state.languages).forEach(lang => {
        if (lang?.key) langKeys.push(lang.key)
      })
      return langKeys
    },
    getFilesLoaded: (state): string[] => state.files,
  },
  actions: {
    t(message: string, params?: object): string {
      if (this.i18n?.global?.t && message && params) return this.i18n?.global?.t(message, params)
      if (this.i18n?.global?.t && message) return this.i18n?.global?.t(message)
      return message
    },
    setI18n(i18n: any): void {
      this.i18n = i18n
    },
    async setCurrentLanguage(newLanguage: string): Promise<void> {
      if (this.language !== newLanguage) {
        this.language = newLanguage
        localStorageInstance.setItem(storageCurrentLanguageKey, newLanguage)
      }
    },
    setFilesLoaded(fileLoaded: string): void {
      if (!this.files.includes(fileLoaded)) {
        this.files.push(fileLoaded)
      }
    },
    resetI18nState(): void {
      this.language = localStorageInstance.getItem('currentLanguage', 'en')
      this.languages = languages
      this.files = []
    },
  },
})
