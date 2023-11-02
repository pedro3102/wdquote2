import { LocationQuery, RouteLocationRaw, RouteParams } from 'vue-router'

export const basicActions = {
  INDEX: 'index',
  LIST: 'list',
  ADD: 'add',
  CREATE: 'create',
  DETAILS: 'details',
  UPDATE: 'update',
  REMOVE: 'remove',
}

export const buildIndexRouteName = (routeName: string): string => `${routeName}-${basicActions.INDEX}`
export const buildListRouteName = (routeName: string): string => `${routeName}-${basicActions.LIST}`
export const buildAddRouteName = (routeName: string): string => `${routeName}-${basicActions.ADD}`
export const buildCreateRouteName = (routeName: string): string => `${routeName}-${basicActions.CREATE}`
export const buildUpdateRouteName = (routeName: string): string => `${routeName}-${basicActions.UPDATE}`
export const buildRemoveRouteName = (routeName: string): string => `${routeName}-${basicActions.REMOVE}`
export const buildDetailsRouteName = (routeName: string): string => `${routeName}-${basicActions.DETAILS}`
export const buildCustomRouteName = (routeName: string): string => `${routeName}`

export const goHome = (): string => '/'

export const buildPath = (routeName: string, param?: string): string => {
  if (routeName) {
    if (param) {
      return `/${routeName}/${param}`
    }
    return `/${routeName}`
  }
  return goHome()
}

export const buildRoute = (routeName: string, params: RouteParams, query: LocationQuery): RouteLocationRaw => ({
  name: routeName,
  params,
  query,
})

export const buildCreateRoute = (routeName: string, params?: object, query?: object): RouteLocationRaw => ({
  name: buildCreateRouteName(routeName),
  params: { ...params },
  query: { ...query },
})

export const buildListRoute = (routeName: string): RouteLocationRaw => ({
  name: buildListRouteName(routeName),
})

export const buildUpdateRoute = (routeName: string, item: object, property?: string, params?: object): RouteLocationRaw => {
  const prop = property || 'id'
  return {
    name: buildUpdateRouteName(routeName),
    params: { [prop]: item ? item[prop] : null, ...params },
  }
}

export const buildDetailsRoute = (routeName: string, item: object, property?, params?: object): RouteLocationRaw => {
  const prop = property || 'id'
  return {
    name: buildDetailsRouteName(routeName),
    params: { [prop]: item ? item[prop] : null, ...params },
  }
}

export const buildRemoveRoute = (routeName: string, item: object, customParams: object = {}, property = 'id'): RouteLocationRaw => ({
  name: buildRemoveRouteName(routeName),
  params: {
    [property]: item[property],
    // name: entityName(item),
    // url: getModule.value,
    ...customParams,
  },
})

export const buildCustomRoute = (routeName: string, params?: object, query?: object): RouteLocationRaw => ({
  name: routeName,
  params: { ...params },
  query: { ...query },
})
