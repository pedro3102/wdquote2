import { maxLength, minValue, required } from '@vuelidate/validators'
const productRules: any = {
  product: {
    code: {
      required,
      maxLength: maxLength(50),
    },
    description: {
      required,
      maxLength: maxLength(255),
    },
    category: {
      required,
    },
    uom: {
      required,
    },
    weight: {
      minValueValue: minValue(0),
    },
  },
}

export default productRules
