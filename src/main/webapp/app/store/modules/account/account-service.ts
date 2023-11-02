import { useAccountStore } from '@/store/modules/account/account-store'
import EntityService from '@/views/entities/common/entity-service'
import LoginService from '@/views/login/login-service'
import { localStorageInstance } from '@/shared/services/storage/local-storage-service'
import { sessionStorageInstance } from '@/shared/services/storage/session-storage-service'
import { useAuthStore } from '@/store/modules/auth/auth-store'
import { RESOURCES } from '@/shared/const/auth-constants'
import { useMenuStore } from '@/store/modules/menu/menu-store'

const apiPath = `${EntityService.apiPath}`

export class AccountService {
  private accountStore

  constructor() {
    this.accountStore = useAccountStore()
    // this.init()
  }

  public resetPassword = async (email: string): Promise<any> =>
    EntityService.post(`${apiPath}/account/reset-password/init`, email, { headers: { 'content-type': 'text/plain' } })

  public hasAnyAuthority(authorities: string[]): boolean {
    if (this.accountStore.account?.authorities) {
      if (this.accountStore.isSystemUser) return true
      return authorities.some(authority => this.accountStore.account?.authorities?.includes(authority))
    }
    return false
  }

  public async hasAnyAuthorityAndCheckAuth(authorities: any): Promise<boolean> {
    let authoritiesToCheck = authorities
    if (typeof authorities === 'string') {
      authoritiesToCheck = [authorities]
    }
    if (!this.accountStore.authenticated || !this.accountStore.account?.authorities) {
      await this.loadAccount()
      if (this.isAuthenticated()) {
        return Promise.resolve(this.hasAnyAuthority(authoritiesToCheck))
      }
      return Promise.resolve(false)
    }
    return Promise.resolve(this.hasAnyAuthority(authoritiesToCheck))
  }

  public isAuthenticated(): boolean {
    return this.accountStore.authenticated
  }

  public async retrieveAccount(): Promise<boolean> {
    try {
      const authStore = useAuthStore()
      const menuStore = useMenuStore()
      const response = await LoginService.getCurrentAccount()
      if (response.status === 200 && response.data) {
        const account = response.data
        this.accountStore.setAuthentication(account)
        await authStore.loadResource(RESOURCES.RESOURCE_MAIN_MENU)
        await menuStore.loadMenu(account)
        return true
      }
    } catch (error) {
      this.accountStore.logout()
    }

    this.accountStore.logout()
    return false
  }

  public async loadAccount() {
    if (this.accountStore.logon) {
      return this.accountStore.logon
    }
    const token = localStorageInstance.getItem('authenticationToken') || sessionStorageInstance.getItem('jhi-authenticationToken')
    if (this.isAuthenticated() && this.userAuthorities && token) {
      return
    }
    const promise = this.retrieveAccount()
    this.accountStore.authenticate(promise)
    promise.then(() => this.accountStore.authenticate(null))
    await promise
  }

  public get userAuthorities(): string[] {
    return this.accountStore.account?.authorities
  }

  public async logoutAccount(): Promise<boolean> {
    localStorageInstance.removeItem('authenticationToken')
    sessionStorageInstance.removeItem('authenticationToken')
    /*    try {
      LoginService.logout().then(() => {
        this.accountStore.logout()
      });
    } catch (error) {}*/
    this.accountStore.logout()
  }
}

let accountService: AccountService | null = null

export const accountServiceInstance = (): AccountService => {
  if (!accountService) accountService = new AccountService()
  return accountService
}

export const hasAnyAuthority = (authorities: any): boolean => accountServiceInstance().hasAnyAuthority(authorities)
