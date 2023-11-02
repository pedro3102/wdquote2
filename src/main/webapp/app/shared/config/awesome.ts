import { createI18n, IntlDateTimeFormats } from 'vue-i18n'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { library } from '@fortawesome/fontawesome-svg-core'
import { faArrowLeft } from '@fortawesome/free-solid-svg-icons/faArrowLeft'
import { faAngleDown } from '@fortawesome/free-solid-svg-icons/faAngleDown'
import { faAsterisk } from '@fortawesome/free-solid-svg-icons/faAsterisk'
import { faBan } from '@fortawesome/free-solid-svg-icons/faBan'
import { faBars } from '@fortawesome/free-solid-svg-icons/faBars'
import { faBell } from '@fortawesome/free-solid-svg-icons/faBell'
import { faBook } from '@fortawesome/free-solid-svg-icons/faBook'
import { faCloud } from '@fortawesome/free-solid-svg-icons/faCloud'
import { faCogs } from '@fortawesome/free-solid-svg-icons/faCogs'
import { faDatabase } from '@fortawesome/free-solid-svg-icons/faDatabase'
import { faEye } from '@fortawesome/free-solid-svg-icons/faEye'
import { faFlag } from '@fortawesome/free-solid-svg-icons/faFlag'
import { faHeart } from '@fortawesome/free-solid-svg-icons/faHeart'
import { faHome } from '@fortawesome/free-solid-svg-icons/faHome'
import { faList } from '@fortawesome/free-solid-svg-icons/faList'
import { faLock } from '@fortawesome/free-solid-svg-icons/faLock'
import { faPencilAlt } from '@fortawesome/free-solid-svg-icons/faPencilAlt'
import { faPlus } from '@fortawesome/free-solid-svg-icons/faPlus'
import { faRoad } from '@fortawesome/free-solid-svg-icons/faRoad'
import { faSave } from '@fortawesome/free-solid-svg-icons/faSave'
import { faSearch } from '@fortawesome/free-solid-svg-icons/faSearch'
import { faSignInAlt } from '@fortawesome/free-solid-svg-icons/faSignInAlt'
import { faSignOutAlt } from '@fortawesome/free-solid-svg-icons/faSignOutAlt'
import { faSort } from '@fortawesome/free-solid-svg-icons/faSort'
import { faSortDown } from '@fortawesome/free-solid-svg-icons/faSortDown'
import { faSortUp } from '@fortawesome/free-solid-svg-icons/faSortUp'
import { faSync } from '@fortawesome/free-solid-svg-icons/faSync'
import { faTachometerAlt } from '@fortawesome/free-solid-svg-icons/faTachometerAlt'
import { faTasks } from '@fortawesome/free-solid-svg-icons/faTasks'
import { faThList } from '@fortawesome/free-solid-svg-icons/faThList'
import { faTimesCircle } from '@fortawesome/free-solid-svg-icons/faTimesCircle'
import { faTimes } from '@fortawesome/free-solid-svg-icons/faTimes'
import { faTrash } from '@fortawesome/free-solid-svg-icons/faTrash'
import { faUser } from '@fortawesome/free-solid-svg-icons/faUser'
import { faUserPlus } from '@fortawesome/free-solid-svg-icons/faUserPlus'
import { faUsers } from '@fortawesome/free-solid-svg-icons/faUsers'
import { faUsersCog } from '@fortawesome/free-solid-svg-icons/faUsersCog'
import { faWrench } from '@fortawesome/free-solid-svg-icons/faWrench'
import { faCalendarAlt } from '@fortawesome/free-solid-svg-icons/faCalendarAlt'
import { faCog } from '@fortawesome/free-solid-svg-icons/faCog'
import { faExternalLink } from '@fortawesome/free-solid-svg-icons/faExternalLink'
import { faEllipsis } from '@fortawesome/free-solid-svg-icons/faEllipsis'
import { faCircleQuestion } from '@fortawesome/free-solid-svg-icons/faCircleQuestion'
import { faCheck } from '@fortawesome/free-solid-svg-icons/faCheck'
import { faCancel } from '@fortawesome/free-solid-svg-icons/faCancel'
import { faFilterCircleXmark } from '@fortawesome/free-solid-svg-icons/faFilterCircleXmark'
import { faLanguage } from '@fortawesome/free-solid-svg-icons/faLanguage'
import { faMapMarker } from '@fortawesome/free-solid-svg-icons/faMapMarker'
import { faBoxesStacked } from '@fortawesome/free-solid-svg-icons/faBoxesStacked'
import { faStore } from '@fortawesome/free-solid-svg-icons/faStore'
import { faCircle } from '@fortawesome/free-solid-svg-icons/faCircle'
import { faDotCircle } from '@fortawesome/free-solid-svg-icons/faDotCircle'
import { faLocation } from '@fortawesome/free-solid-svg-icons/faLocation'
import { faDoorOpen } from '@fortawesome/free-solid-svg-icons/faDoorOpen'
import { faRetweet } from '@fortawesome/free-solid-svg-icons/faRetweet'
import { faRuler } from '@fortawesome/free-solid-svg-icons/faRuler'
import { faMaximize } from '@fortawesome/free-solid-svg-icons/faMaximize'
import { faWarehouse } from '@fortawesome/free-solid-svg-icons/faWarehouse'
import { faBorderAll } from '@fortawesome/free-solid-svg-icons/faBorderAll'
import { faMap } from '@fortawesome/free-solid-svg-icons/faMap'
import { faIndent } from '@fortawesome/free-solid-svg-icons/faIndent'
import { faUsersBetweenLines } from '@fortawesome/free-solid-svg-icons/faUsersBetweenLines'
import { faIdCard } from '@fortawesome/free-solid-svg-icons/faIdCard'
import { faSpinner } from '@fortawesome/free-solid-svg-icons/faSpinner'
import { faThumbtack } from '@fortawesome/free-solid-svg-icons/faThumbtack'
import { faTruckFast } from '@fortawesome/free-solid-svg-icons/faTruckFast'
import { faCheckDouble } from '@fortawesome/free-solid-svg-icons/faCheckDouble'
import { faWarning } from '@fortawesome/free-solid-svg-icons/faWarning'

const datetimeFormats: IntlDateTimeFormats = {
  en: {
    short: {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: 'numeric',
      minute: 'numeric',
    },
    medium: {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      weekday: 'short',
      hour: 'numeric',
      minute: 'numeric',
    },
    long: {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      weekday: 'long',
      hour: 'numeric',
      minute: 'numeric',
    },
  },
  fr: {
    short: {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: 'numeric',
      minute: 'numeric',
    },
    medium: {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      weekday: 'short',
      hour: 'numeric',
      minute: 'numeric',
    },
    long: {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      weekday: 'long',
      hour: 'numeric',
      minute: 'numeric',
    },
  },
  es: {
    short: {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: 'numeric',
      minute: 'numeric',
    },
    medium: {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      weekday: 'short',
      hour: 'numeric',
      minute: 'numeric',
    },
    long: {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      weekday: 'long',
      hour: 'numeric',
      minute: 'numeric',
    },
  },
  // jhipster-needle-i18n-language-date-time-format - JHipster will add/remove format options in this object
}

export function initFortAwesome(vue) {
  vue.component('font-awesome-icon', FontAwesomeIcon)

  library.add(
    faArrowLeft,
    faAngleDown,
    faAsterisk,
    faBan,
    faBars,
    faBell,
    faBook,
    faCloud,
    faCogs,
    faDatabase,
    faEye,
    faFlag,
    faHeart,
    faHome,
    faList,
    faLock,
    faPencilAlt,
    faPlus,
    faRoad,
    faSave,
    faSearch,
    faSignInAlt,
    faSignOutAlt,
    faSort,
    faSortDown,
    faSortUp,
    faSync,
    faTachometerAlt,
    faTasks,
    faThList,
    faTimes,
    faTimesCircle,
    faTrash,
    faUser,
    faUserPlus,
    faUsers,
    faUsersCog,
    faWrench,
    faCalendarAlt,
    faCog,
    faExternalLink,
    faCircleQuestion,
    faEllipsis,
    faCheck,
    faCancel,
    faFilterCircleXmark,
    faLanguage,
    faMapMarker,
    faBoxesStacked,
    faStore,
    faCircle,
    faDotCircle,
    faLocation,
    faDoorOpen,
    faMapMarker,
    faBoxesStacked,
    faRetweet,
    faRuler,
    faMaximize,
    faWarehouse,
    faBorderAll,
    faMap,
    faIndent,
    faUsersBetweenLines,
    faIdCard,
    faSpinner,
    faThumbtack,
    faRetweet,
    faTruckFast,
    faCheckDouble,
    faWarning
  )
}

export function initI18N(opts: any = {}) {
  return createI18n({
    missingWarn: false,
    fallbackWarn: false,
    legacy: false,
    datetimeFormats,
    silentTranslationWarn: true,
    ...opts,
  })
}
