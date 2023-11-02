import { maxLength, minValue } from '@vuelidate/validators'

const inventoryRules: any = {
  inventory: {
    reorderPoint: {
      minValue: minValue(0),
    },
    vendorLeadTime: {
      minValue: minValue(0),
    },
    shelf: {
      maxLength: maxLength(50),
    },
  },
}

export default inventoryRules
