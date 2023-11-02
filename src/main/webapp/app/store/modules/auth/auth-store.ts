import { defineStore } from 'pinia'
import { toRaw } from 'vue'
import AuthService from '@/store/modules/auth/auth-service'
import { IAuthResource } from '@/store/modules/auth/auth-resource-model'
import { RESOURCES } from '@/shared/const/auth-constants'

export interface IAuthState {
  resources: object
}

export const initialAuthState: IAuthState = {
  resources: {},
}

export const useAuthStore = defineStore({
  id: 'authorization',
  state: (): IAuthState => ({ ...initialAuthState }),
  getters: {
    getResources: state => state.resources,
    getResource:
      state =>
      (resourceName: string): object =>
        state.resources[resourceName],
    getResourceAccess:
      state =>
      (resourceName: string, action: string): string => {
        const resource = toRaw(state.resources[resourceName])
        if (resource?.actions && resource.actions[action] && resource.actions[action].roles) {
          return resource.actions[action].roles.join()
        }
        return ''
      },
    getMenuResourceAccess:
      state =>
      (action: string): string => {
        const resource = toRaw(state.resources[RESOURCES.RESOURCE_MAIN_MENU])
        if (resource?.actions && resource.actions[action] && resource.actions[action].roles) {
          return resource.actions[action].roles.join()
        }
        return ''
      },
  },
  actions: {
    async loadResource(name: string): Promise<void> {
      const resource = toRaw(this.getResource(name))
      if (!resource) {
        const { data } = await AuthService.loadResource(name)
        if (data) {
          this.addResource(data)
        }
      }
      return Promise.resolve()
    },
    addResource(resource: IAuthResource): void {
      if (resource?.name) {
        this.resources[resource.name] = resource
      }
    },
    resetAuthState(): void {
      this.resources = {}
    },
  },
})
