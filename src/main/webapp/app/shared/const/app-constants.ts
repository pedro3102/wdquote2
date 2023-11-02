import { dateTimeFormats } from '@/shared/const/global-constants'

const appConstants = {
  /*  VERSION: process.env.VUE_APP_VERSION,
  GIT_BRANCH_NAME: process.env.VUE_APP_GIT_BRANCH_NAME,
  GIT_COMMIT_HASH: process.env.VUE_APP_GIT_COMMIT_HASH,
  GIT_COMMIT_DATETIME: process.env.VUE_APP_GIT_COMMIT_DATETIME,
  GOOGLE_MAPS_URL: process.env.VUE_APP_GOOGLE_MAPS_URL,
  DEBUG_INFO_ENABLED: false,*/
  APP: {
    /*    APP_NAME: 'EHR',
    APP_SUFFIX_NAME: 'X',
    APP_SHORT_NAME: 'M',*/
    APP_STORAGE_PREFIX: 'quote',
    PAGE_SIZE: 15,
    PAGE_MAX_SAFE_SIZE: 100000,
    MAX_FRACTION_DIGITS: 2,
    DATE_FORMAT: dateTimeFormats.SHORT_DATE,
    DATE_TIME_FORMAT: 'MM/dd/yyyy hh:mm aa',
    DATE_ISO_FORMAT: dateTimeFormats.ISO_DATE,
    TIME_FORMAT: 'hh:mm aa',
    TIME_FORMAT_SHORT: 'HH:mm',
  },
}

export default appConstants
