import { maxLength, required } from '@vuelidate/validators'
const systemRules: any = {
  system: {
    name: {
      required,
      maxLength: maxLength(50),
    },
  },
}

export default systemRules
