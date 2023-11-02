import {maxLength, required} from '@vuelidate/validators'

const movementTypeRules: any = {
  movementType: {
    code: {
      required,
      maxLength: maxLength(3),
    },
    name: {
      required,
      maxLength: maxLength(50),
    },
    counterpart: {
      required,
    },
  },
}

export default movementTypeRules
