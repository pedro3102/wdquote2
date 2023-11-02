import appConstants from '@/shared/const/app-constants'
import { StorageService } from './storage-service'

export class SessionStorageService extends StorageService {
  public constructor(public prefix: string) {
    super(window.sessionStorage, prefix)
  }
}

export const sessionStorageInstance = new SessionStorageService(appConstants.APP.APP_STORAGE_PREFIX)
