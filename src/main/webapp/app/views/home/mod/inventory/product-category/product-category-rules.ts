import { maxLength, required } from '@vuelidate/validators'
const productCategoryRules: any = {
  productCategory: {
    name: {
      required,
      maxLength: maxLength(50),
    },
  },
}

export default productCategoryRules
