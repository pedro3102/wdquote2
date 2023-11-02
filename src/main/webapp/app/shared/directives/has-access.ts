import { useAccountStore } from '@/store/modules/account/account-store'
import { hasAnyAuthority } from '@/store/modules/account/account-service'

const check = (access, values, fn) => {
  if (access) {
    const result = access.replace(/\s+/g, '').split(',')
    if (!fn(result)) {
      return true
    }
  } else {
    return true
  }
  return false
}

const hasAccess = (el, binding) => {
  const accountStore = useAccountStore()
  const hideByAccess = check(binding.value.access, accountStore.account, hasAnyAuthority)
  if (hideByAccess) el.classList.add('hidden')
  else el.classList.remove('hidden')
}

export default {
  mounted(el, binding) {
    el.classList.add('hidden')
    if (!binding.modifiers.static) {
      el.classList.add('hidden')
    } else {
      hasAccess(el, binding)
    }
  },
  updated(el, binding) {
    if (binding.value !== binding.oldValue) {
      hasAccess(el, binding)
    }
  },
}
