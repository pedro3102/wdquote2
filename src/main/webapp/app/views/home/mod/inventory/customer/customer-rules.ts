import { maxLength, required } from '@vuelidate/validators'
const customerRules: any = {
  customer: {
    code: {
      required,
      maxLength: maxLength(50),
    },
    name: {
      required,
      maxLength: maxLength(255),
    },
    deliveryZone: {
      required,
    },
    taxAreaCode: {
      required,
    },
  },
}

export default customerRules
