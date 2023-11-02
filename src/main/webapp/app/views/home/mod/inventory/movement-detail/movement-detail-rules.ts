import {minValue, required} from '@vuelidate/validators'

const movementDetailRules: any = {
  movementDetail: {
    product: {
      required,
    },
    unitCost: {
      required,
      minValue: minValue(0.00001),
    },
    salePrice: {
      minValue: minValue(0),
    },
    qty: {
      required,
      minValue: minValue(0.00001),
    },
  },
}

export default movementDetailRules
