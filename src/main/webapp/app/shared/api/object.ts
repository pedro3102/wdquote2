export const fetchFromObject = (obj, prop) => {
  if (!obj) {
    return ''
  }

  const index = prop.indexOf('.')

  if (index > -1) {
    return fetchFromObject(obj[prop.substring(0, index)], prop.substr(index + 1))
  }

  return obj ? obj[prop] : ''
}

const useFetchFromObject = () => ({
  fetchFromObject,
})

export default useFetchFromObject
