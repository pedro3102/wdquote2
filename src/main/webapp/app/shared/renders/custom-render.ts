import appConstants from '@/shared/const/app-constants'
import { format, getTime, parseISO } from 'date-fns'
import { fetchFromObject } from '@/shared/api/object'
import { useI18n } from 'vue-i18n'
import { LocationType, OperationCounterpart, OperationType, PrimeSeverities, StockStatus } from '@/shared/const/entity-constants'
import { ROLES } from '@/shared/const/auth-constants'

const getSeparator = (street, street2, city, state, zip) => ((street || street2 || city) && (state || zip) ? ', ' : '')

const getStreet = value => value.street || ''
const getStreet2 = value => value.street2 || ''
const getCity = value => (value.city ? value.city.name : '')
const getState = value => (value.state ? value.state.code : '')
const getZip = value => value.zip || ''

/**
 * Custom render for Address.
 * @param value
 * @returns {string}
 */
const addressRender = value => {
  if (value) {
    const street = getStreet(value)
    const street2 = getStreet2(value)
    const city = getCity(value)
    const state = getState(value)
    const zip = getZip(value)
    const SEPARATOR = getSeparator(street, street2, city, state, zip)
    return `${street} ${street2} ${city}${SEPARATOR} ${state} ${zip}`
  }
  return ''
}

/**
 * Custom render for Date
 * @param date The date.
 * @param customFormat A custom format, the default value is: 'MM/dd/yyyy'
 * @returns {string}
 */
const dateRender = (date, customFormat) => {
  if (date) {
    const newDate = new Date(date)
    const dtDateOnly = new Date(newDate.valueOf() + newDate.getTimezoneOffset() * 60 * 1000)
    return format(dtDateOnly, customFormat)
  }
  return ''
}

/**
 * Custom render for Date
 * @param date The date.
 * @param customFormat A custom format, the default value is: 'yyyy-MM-ddTHH:mm'
 * @returns {string}
 */
const dateTimeRender = (date, customFormat) => {
  if (date) {
    const newDate = new Date(date)
    const dateString = format(newDate, customFormat)
    const time = format(newDate.getTime(), 'HH:mm')
    return `${dateString}T${time}`
  }
  return ''
}

/**
 * Custom render for UTC Date
 * @param date The date.
 * @param customFormat A custom format, the default value is: 'MM/dd/yyyy'
 * @returns {string}
 */
const dateUTCRender = (date, customFormat) => {
  if (date) {
    const newDate = new Date(date)
    return format(
      Date.UTC(
        newDate.getFullYear(),
        newDate.getMonth(),
        newDate.getDate(),
        newDate.getHours(),
        newDate.getMinutes(),
        newDate.getSeconds()
      ),
      customFormat
    )
  }
  return ''
}

/**
 * Custom render for UTC Time
 * @param date The date.
 * @param customFormat A custom format, the default value is: 'hh:mm aa'
 * @returns {string}
 */
const timeUTCRender = (date, customFormat) => {
  if (date) {
    return format(
      new Date(date.getUTCFullYear(), date.getUTCMonth(), date.getUTCDay(), date.getUTCHours(), date.getUTCMinutes(), date.getUTCSeconds()),
      customFormat
    )
  }
  return ''
}

/**
 * Custom render for Time
 * @param time The time.
 * @param customFormat A custom format, the default value is: 'MM/dd/yyyy'
 * @returns {string}
 */
const timeRender = (time, customFormat) => {
  const dateString = format(new Date(), 'yyyy-MM-dd')
  return time ? format(getTime(parseISO(`${dateString}T${time}`)), customFormat) : ''
}

const renderPrimeTag = (data: string, color: string) => {
  return `<span class=" m-1 p-tag p-component p-tag-${color}" data-pc-name="tag" data-pc-section="root">
                      <span class="p-tag-value" data-pc-section="value">${data}</span>
                    </span>`
}

const booleanRender = data => {
  const { t } = useI18n()
  if (data) return renderPrimeTag(t('entity.action.yesUppercase'), PrimeSeverities.SUCCESS)
  if (!data) return renderPrimeTag(t('entity.action.noUppercase'), PrimeSeverities.DANGER)
  return ''
}

const locationTypeRender = data => {
  switch (data) {
    case LocationType.WAREHOUSE:
      return renderPrimeTag(data, PrimeSeverities.SUCCESS)
    case LocationType.WIP:
      return renderPrimeTag(data, PrimeSeverities.INFO)
    case LocationType.ON_PAINT:
      return renderPrimeTag(data, PrimeSeverities.DANGER)
    default:
      return ''
  }
}

const operationTypeRender = data => {
  switch (data) {
    case OperationType.IN:
      return renderPrimeTag(data, PrimeSeverities.SUCCESS)
    case OperationType.OUT:
      return renderPrimeTag(data, PrimeSeverities.DANGER)
    default:
      return ''
  }
}

const operationCounterpartRender = data => {
  switch (data) {
    case OperationCounterpart.CUSTOMER:
      return renderPrimeTag(data, PrimeSeverities.SUCCESS)
    case OperationCounterpart.VENDOR:
      return renderPrimeTag(data, PrimeSeverities.DANGER)
    case OperationCounterpart.LOCATION:
      return renderPrimeTag(data, PrimeSeverities.INFO)
    default:
      return ''
  }
}

const userAuthoritiesRender = data => {
  let result = ''
  if (data) {
    data.forEach(auth => {
      if (auth.name === ROLES.ROLE_ADMIN) {
        result += renderPrimeTag(auth.name, PrimeSeverities.DANGER)
      } else {
        result += renderPrimeTag(auth.name, PrimeSeverities.INFO)
      }
    })
  }
  return result
}

const serviceNameRender = service => {
  if (service) {
    let modifier = ''
    if (service.modifier) {
      modifier = `/${fetchFromObject(service, 'modifier.code')}`
    }
    return `${fetchFromObject(service, 'cpt.code')}${modifier} - ${fetchFromObject(service, 'name')}`
  }
  return ''
}

const healthPlanName = healthPlan => {
  if (healthPlan) {
    return `${healthPlan.name} (${fetchFromObject(healthPlan, 'managedCareInsurance.name')})`
  }
  return ''
}

const phone = phoneNumber => {
  if (phoneNumber) {
    const cleaned = `${phoneNumber}`.replace(/\D/g, '')
    const match = cleaned.match(/^(1|)?(\d{3})(\d{3})(\d{4})$/)
    if (match) {
      const intlCode = match[1] ? '+1 ' : ''
      return [intlCode, '(', match[2], ') ', match[3], '-', match[4]].join('')
    }
  }
  return ''
}
const renderStockStatus = (stockStatus: StockStatus) => {
  if (stockStatus === StockStatus.OUT_STOCK) {
    return renderPrimeTag(StockStatus.OUT_STOCK, PrimeSeverities.DANGER)
  }
  if (stockStatus === StockStatus.LOW_STOCK) {
    return renderPrimeTag(StockStatus.LOW_STOCK, PrimeSeverities.WARNING)
  }
  if (stockStatus === StockStatus.IN_STOCK) {
    return renderPrimeTag(StockStatus.IN_STOCK, PrimeSeverities.SUCCESS)
  }
}

const customRender = {
  /**
   * This is a deprecated function, please make use of booleanRender function.
   * @param value
   * @param size
   * @returns {string}
   */
  boolean: value => booleanRender(value),
  locationType: value => locationTypeRender(value),
  operationType: value => operationTypeRender(value),
  operationCounterpart: value => operationCounterpartRender(value),
  userAuthorities: value => userAuthoritiesRender(value),
  renderPrimeTag: (data, color) => renderPrimeTag(data, color),
  renderStockStatus: (stockStatus: StockStatus) => renderStockStatus(stockStatus),
  currency: value => `$${value}`, // translationService.n(value, 'currency')
  fullName: (firstName, lastName) => `${firstName} ${lastName}`, // `${lastName}, ${firstName}`,
  address: value => addressRender(value),
  status: (value, size) => statusRender(value, size),
  docStatus: (value, size) => docStatusRender(value, size),
  docStatusColor: value => docStatusColor(value),
  docTypeColor: value => docTypeColor(value),
  module: value => moduleRender(value),
  modules: value => moduleRender(value),
  date: (value, customFormat = appConstants.APP.DATE_FORMAT) => dateRender(value, customFormat),
  dateTime: (value, customFormat = appConstants.APP.DATE_ISO_FORMAT) => dateTimeRender(value, customFormat),
  dateUTC: (value, customFormat = appConstants.APP.DATE_FORMAT) => dateUTCRender(value, customFormat),
  time: (value, customFormat = appConstants.APP.TIME_FORMAT) => timeRender(value, customFormat),
  timeUTC: (value, customFormat = appConstants.APP.TIME_FORMAT) => timeUTCRender(value, customFormat),
  gender: value => genderRender(value),
  diagnosis: value => diagnosisRender(value),
  serviceName: service => serviceNameRender(service),
  patientName: patient => patientNameRender(patient),
  chartRender: chart => chartRender(chart),
  healthPlanName: healthPlan => healthPlanName(healthPlan),
  /**
   * Method to abbreviate the text given
   */
  abbreviate(value, length = 100, append = '...') {
    if (value.length < length) {
      return value
    }
    return value ? value.substring(0, length / 2) + append : ''
  },
  /**
   * Format the phone number.
   * @param phoneNumber
   * @returns {*}
   */
  phone: phoneNumber => phone(phoneNumber),
}

export default customRender
