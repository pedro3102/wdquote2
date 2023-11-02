<template>
  <div class="bg-image surface-ground flex align-items-center justify-content-center min-h-screen min-w-screen overflow-hidden">
    <BaseProgressSpinner v-if="loading" />
    <div v-else class="flex flex-column align-items-center justify-content-center">
      <div
        style="
          border-radius: 56px;
          padding: 0.3rem;
          background: linear-gradient(180deg, var(--primary-color) 10%, rgba(33, 150, 243, 0) 30%);
        "
      >
        <div class="w-full surface-card py-8 px-5 sm:px-8" style="border-radius: 53px">
          <div class="text-center mb-5">
            <img src="/content/images/login/logo-sap-dark-100x100.png" alt="Image" height="80" class="mb-3" />
            <div class="text-900 text-3xl font-medium mb-3">SAP Windows & Doors</div>
            <span class="text-600 font-medium" v-text="$t('login.subTitle')" />
          </div>

          <div>
            <label for="email1" class="block text-900 text-xl font-medium mb-2" v-text="$t('login.form.login')" />
            <InputText
              id="username"
              v-model="validator.userLogin.$model.username"
              class="w-full md:w-30rem mb-5"
              :class="{ 'p-invalid': validator.userLogin.username.$invalid }"
              type="text"
              :placeholder="$t('login.form[\'login.placeholder\']')"
              style="padding: 1rem"
            />

            <label for="password1" class="block text-900 font-medium text-xl mb-2" v-text="$t('login.form.password')" />
            <Password
              id="password1"
              v-model="validator.userLogin.$model.password"
              class="w-full mb-3"
              :class="{ 'p-invalid': validator.userLogin.password.$invalid }"
              :placeholder="$t('login.form[\'password.placeholder\']')"
              :toggleMask="true"
              inputClass="w-full"
              :inputStyle="{ padding: '1rem' }"
            ></Password>

            <div class="flex align-items-center justify-content-between mb-5 gap-5">
              <div class="flex align-items-center">
                <Checkbox v-model="userLogin.rememberMe" id="rememberme" binary class="mr-2"></Checkbox>
                <label for="rememberme" v-text="$t('login.form.rememberMe')" />
              </div>
              <a
                class="font-medium no-underline ml-2 text-right cursor-pointer"
                style="color: var(--primary-color)"
                v-text="$t('login.password.forgot')"
              />
            </div>
            <Button
              :label="$t('login.form.button')"
              class="w-full p-3 text-xl"
              @click="authenticate"
              :disabled="validator.userLogin.$invalid"
            >
            </Button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { useLayout } from '@/shared/composables'
import InputText from 'primevue/inputtext'
import Password from 'primevue/password'
import Checkbox from 'primevue/checkbox'
import Button from 'primevue/button'
import { reactive, computed, inject, defineComponent, ref, Ref } from 'vue'
import { ILogin, Login } from './login-model'
import { NavigationGuardNext, RouteLocationNormalized, useRouter } from 'vue-router'
import { useVuelidate } from '@vuelidate/core'
import loginRules from '@/views/login/login-rules'
import LoginService from '@/views/login/login-service'
import { localStorageInstance } from '@/shared/services/storage/local-storage-service'
import { sessionStorageInstance } from '@/shared/services/storage/session-storage-service'
import { dashBoardRouteName } from '@/views/home/dashboard/dashboard-entity'
import { AccountService } from '@/store/modules/account/account-service'
import { getHomePath } from '@/views/home/home-route'
import BaseProgressSpinner from '@/shared/components/ui/BaseProgressSpinner.vue'
import { useCustomToast } from '@/shared/composables/custom-toast'

export default defineComponent({
  name: 'Login',
  components: {
    InputText,
    Password,
    Checkbox,
    Button,
    BaseProgressSpinner,
  },
  beforeRouteEnter(to: RouteLocationNormalized, from: RouteLocationNormalized, next: NavigationGuardNext) {
    next((vm: any) => {
      vm.loading = true
      vm.accountService
        .retrieveAccount()
        .then(() => {
          if (vm.authenticated) {
            vm.$router.push(getHomePath())
          } else {
            vm.loading = false
          }
        })
        .catch(error => {
          vm.loading = false
        })
    })
  },
  setup() {
    const customToast = useCustomToast()
    const accountService = inject<AccountService>('accountService')
    const { layoutConfig } = useLayout()
    const userLogin: ILogin = reactive(new Login())
    const router = useRouter()
    const authenticated = computed<boolean>(() => accountService.isAuthenticated())
    const loading: Ref<boolean> = ref(false)

    const validator = useVuelidate(loginRules, { userLogin })

    const authenticate = async (): Promise<void> => {
      try {
        if (userLogin.username && userLogin.password) {
          const loginResult = await LoginService.login(userLogin)
          const bearerToken = loginResult.headers.authorization
          if (bearerToken && bearerToken.slice(0, 7) === 'Bearer ') {
            const jwt = bearerToken.slice(7, bearerToken.length)
            if (userLogin.rememberMe) {
              localStorageInstance.setItem('authenticationToken', jwt)
              sessionStorageInstance.removeItem('authenticationToken')
            } else {
              sessionStorageInstance.setItem('authenticationToken', jwt)
              localStorageInstance.removeItem('authenticationToken')
            }
          }
        }
        await router.push({ name: dashBoardRouteName })
      } catch (exception: any) {
        console.log(exception)
        if ((exception?.response, exception?.response?.status)) {
          customToast.responseErrorToast(exception.response.status)
        }
      }
    }

    return {
      validator,
      authenticated,
      userLogin,
      accountService,
      loading,
      authenticate,
    }
  },
})
</script>

<style scoped>
.pi-eye {
  transform: scale(1.6);
  margin-right: 1rem;
}

.pi-eye-slash {
  transform: scale(1.6);
  margin-right: 1rem;
}

.bg-image {
  background-image: url(/content/images/login.webp);
  background-size: cover;
}
</style>
