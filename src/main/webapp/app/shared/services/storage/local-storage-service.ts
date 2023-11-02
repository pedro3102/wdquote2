import { StorageService } from './storage-service';
import appConstants from '@/shared/const/app-constants';

export class LocalStorageService extends StorageService {
  public constructor(public prefix: string) {
    super(window.localStorage, prefix);
  }
}

export const localStorageInstance = new LocalStorageService(appConstants.APP.APP_STORAGE_PREFIX);
