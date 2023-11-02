import {maxLength, required} from '@vuelidate/validators'

const stockPositionRules: any = {
  stockPosition: {
    name: {
      required,
      maxLength: maxLength(50),
    },
    description: {
      required,
      maxLength: maxLength(255),
    },
  },
}

export default stockPositionRules
