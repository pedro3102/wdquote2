import { maxLength, required } from '@vuelidate/validators'
const languageRules: any = {
  language: {
    name: {
      required,
      maxLength: maxLength(25),
    },
    code: {
      required,
      maxLength: maxLength(3),
    },
  },
}

export default languageRules
