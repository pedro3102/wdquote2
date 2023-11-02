import { required } from '@vuelidate/validators'

const unitOfMeasureConversionRules: any = {
  unitOfMeasureConversion: {
    uom: {
      required,
    },
    uomEquivalent: {
      required,
    },
    conversionFactor: {
      required,
    },
  },
}

export default unitOfMeasureConversionRules
