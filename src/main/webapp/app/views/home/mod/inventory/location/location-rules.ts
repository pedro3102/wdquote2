import {maxLength, required} from '@vuelidate/validators'

const locationRules: any = {
  location: {
    code: {
      required,
      maxLength: maxLength(5),
    },
    name: {
      required,
      maxLength: maxLength(50),
    },
    locationType: {
      required,
    },
  },
}

export default locationRules
