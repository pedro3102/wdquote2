import { maxLength, required } from '@vuelidate/validators'
const systemModelRules: any = {
  systemModel: {
    name: {
      required,
      maxLength: maxLength(50),
    },
    description: {
      required,
      maxLength: maxLength(255),
    },
    system: {
      required,
    },
  },
}

export default systemModelRules
