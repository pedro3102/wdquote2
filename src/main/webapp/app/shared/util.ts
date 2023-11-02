import globalConstants from '@/shared/const/global-constants'
import appConstants from '@/shared/const/app-constants'
import { format } from 'date-fns'

export const toggleBodyClass = (addRemoveClass, className) => {
  const el = document.body
  if (addRemoveClass === 'addClass') {
    el.classList.add(className)
  } else {
    el.classList.remove(className)
  }
}

/**
 * Build the select options object (value, text)
 * @param array The Array to populate in the Select Options.
 * @param object The Object to find.
 * @param property The value property name.
 * @returns {*}
 */
export const toSelectOptions = (array, object, property) =>
  array.map(element => {
    if (object && element[property] === object[property]) {
      Object.assign(element, {
        ...object,
      })
    }
    return element
  })

/**
 * Add to the target all the params
 * @deprecated Since version 0.0.1. Will be deleted in version 1.0.0 Use bar instead.
 *
 * Please make use of this replacement {@link use-request#addQueryParams}.
 *
 * @param target url.
 * @param params the object tu extract data .
 * @returns {*}
 */
export const addParamsUrl = (target, params) => {
  let copy = target
  Object.entries(params).forEach(([key, param], index, arr) => {
    copy = `${copy}${key}=${param}${arr[index + 1] ? '&' : ''}`
  })
  return copy
}

export const validateEmail = email => {
  // eslint-disable-next-line no-useless-escape,max-len
  const re =
    /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
  return re.test(String(email).toLowerCase())
}

export const getFullGoogleMapsUrl = address => `${globalConstants.GOOGLE_MAPS_URL}/?q=${address}`

export const assignParam = (object, param, value) => {
  if (value) {
    Object.assign(object, { ...object }, { [param]: value })
  }
}

/**
 * Convert minutes to Hours and Minutes
 *
 * @param data Minutes to convert.
 * @param params the object tu extract data .
 * @returns string wh
 */
export const toHoursAndMinutes = data => {
  function padTo2Digits(num) {
    return num.toString().padStart(2, '0')
  }

  if (Number.isInteger(data)) {
    const minutes = data % 60
    const hours = Math.floor(data / 60)

    return `${padTo2Digits(hours)}:${padTo2Digits(minutes)}`
  }
  return data
}

export const isObject = item => item && typeof item === 'object' && !Array.isArray(item)

export const objectIsEmpty = obj => !obj && Object.entries(obj).length === 0

export const mergeDeep = (target, source) => {
  const output = { ...target }
  if (isObject(target) && isObject(source)) {
    Object.keys(source).forEach(key => {
      if (isObject(source[key])) {
        if (!(key in target)) Object.assign(output, { [key]: source[key] })
        else {
          output[key] = mergeDeep(target[key], source[key])
        }
      } else {
        Object.assign(output, { [key]: source[key] })
      }
    })
  }
  return output
}

export const includesLowerCase = (element, patter) =>
  element && patter ? element.toString().toLowerCase().includes(patter.toString().toLowerCase()) : false

export default {
  toggleBodyClass,
  toSelectOptions,
  addParamsUrl,
  validateEmail,
  getFullGoogleMapsUrl,
  assignParam,
  toHoursAndMinutes,
}
