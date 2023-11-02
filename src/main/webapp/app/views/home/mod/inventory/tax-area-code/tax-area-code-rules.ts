import { maxLength, required } from '@vuelidate/validators'
const taxAreaCodeRules: any = {
  taxAreaCode: {
    code: {
      required,
      maxLength: maxLength(100),
    },
    name: {
      required,
      maxLength: maxLength(100),
    },
  },
}

export default taxAreaCodeRules
