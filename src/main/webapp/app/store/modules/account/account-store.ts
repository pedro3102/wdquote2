import { defineStore } from 'pinia'
import { IUser } from '@/views/home/admin/user-management/user.model'
import { useAuthStore } from '@/store/modules/auth/auth-store'
import { useLangStore } from '@/store/modules/i18n/translation-store'
import { ROLES } from '@/shared/const/auth-constants'

export interface AccountStateStorable {
  logon: boolean
  user: IUser | null
  authenticated: boolean
}

export const defaultAccountState: AccountStateStorable = {
  logon: null,
  user: null,
  authenticated: false,
}

export const useAccountStore = defineStore('account', {
  state: (): AccountStateStorable => ({ ...defaultAccountState }),
  getters: {
    account: state => state.user,
    isSystemUser: state => state.user.authorities.some(authority => authority === ROLES.ROLE_SYSTEM),
  },
  actions: {
    authenticate(promise) {
      this.logon = promise
    },
    setAuthentication(identity) {
      this.user = identity
      this.authenticated = true
      this.logon = null
    },
    logout() {
      const authStore = useAuthStore()
      const langStore = useLangStore()
      this.user = null
      this.authenticated = false
      this.logon = null
      authStore.resetAuthState()
      langStore.resetI18nState()
    },
    resetAccountState(): void {
      this.logon = null
      this.user = null
      this.authenticated = false
    },
  },
})
