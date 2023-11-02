import { maxLength, required } from '@vuelidate/validators'
const vendorRules: any = {
  vendor: {
    code: {
      required,
      maxLength: maxLength(100),
    },
    name: {
      required,
      maxLength: maxLength(100),
    },
    address: {
      required,
    },
  },
}

export default vendorRules
