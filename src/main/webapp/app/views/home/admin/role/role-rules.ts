import { required } from '@vuelidate/validators'

const roleRules = {
  role: {
    id: { required },
    name: { required },
  },
}

export default roleRules
