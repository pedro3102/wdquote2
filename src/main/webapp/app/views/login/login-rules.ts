import { maxLength, required } from '@vuelidate/validators';

const loginRules = {
  userLogin: {
    username: { required, maxLength: maxLength(40) },
    password: { required, maxLength: maxLength(40) },
    rememberMe: {},
  },
};

export default loginRules;
