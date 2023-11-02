import { maxLength, required, email, minLength } from '@vuelidate/validators'

const customerContactRules: any = {
  customerContact: {
    code: {
      required,
      maxLength: maxLength(10),
    },
    name: {
      required,
      maxLength: maxLength(100),
    },
    email: {
      required,
      email,
      minLength: minLength(5),
      maxLength: maxLength(50),
    },
    customer: {
      required,
    },
    language: {
      required,
    },
  },
}

export default customerContactRules
