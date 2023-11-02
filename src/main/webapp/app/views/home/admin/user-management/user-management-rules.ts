import { email, maxLength, minLength, required } from '@vuelidate/validators'
const loginValidator = (value: string) => {
  if (!value) {
    return true
  }
  return /^[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$|^[_.@A-Za-z0-9-]+$/.test(value)
}
const userRules: any = {
  user: {
    login: {
      required,
      maxLength: maxLength(25),
      pattern: loginValidator,
    },
    firstName: {
      maxLength: maxLength(50),
    },
    lastName: {
      maxLength: maxLength(50),
    },
    email: {
      required,
      email,
      minLength: minLength(5),
      maxLength: maxLength(50),
    },
    authorities: {
      required,
    },
  },
}

export default userRules
