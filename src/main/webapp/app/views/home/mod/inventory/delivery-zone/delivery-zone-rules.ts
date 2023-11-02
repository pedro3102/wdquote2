import { maxLength, required } from '@vuelidate/validators'
const deliveryZoneRules: any = {
  deliveryZone: {
    code: {
      required,
      maxLength: maxLength(100),
    },
    name: {
      required,
      maxLength: maxLength(100),
    },
  },
}

export default deliveryZoneRules
