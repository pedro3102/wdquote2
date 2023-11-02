import { maxLength, required } from '@vuelidate/validators'
const unitOfMeasureRules: any = {
  unitOfMeasure: {
    name: {
      required,
      maxLength: maxLength(50),
    },
    abbreviation: {
      required,
      maxLength: maxLength(10),
    },
  },
}

export default unitOfMeasureRules
