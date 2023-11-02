import { computed } from 'vue'

export interface IModelOptions {
  valueName?: string
  uppercase?: boolean
}

/**
 * This utils function allow to handle the Field common functionalities.
 * @param props {Object} The component properties.
 * @param emit.
 * @param options {IModelOptions} The options.
 * @returns {Object}
 */
export const useModel = (props, emit, options?: IModelOptions) => {
  /**
   * Handle the source object reactivity.
   */
  const valueNameOption = computed(() => options?.valueName || 'modelValue')
  const manageUpper = computed(() => {
    if (options?.uppercase === false && props[valueNameOption.value]) return props[valueNameOption.value].toLowerCase()
    if (options?.uppercase === true && props[valueNameOption.value]) return props[valueNameOption.value].toUpperCase()
    return props[valueNameOption.value]
  })
  const model = computed({
    get: () => manageUpper.value,
    set: value => emit(`update:${valueNameOption.value}`, value),
  })

  /**
   * Return a field name using a baseName, util in schema view.
   * @param baseName {String} The base name.
   * @returns {string|*} The concat basedName with an ID or "" in case of null values.
   */
  const buildFieldName = baseName => {
    const uniqueCounter = +new Date() % 10000
    if (baseName) {
      return `${baseName}-${uniqueCounter}`
    }
    return uniqueCounter
  }

  return {
    model,
    buildFieldName,
  }
}

export default useModel
