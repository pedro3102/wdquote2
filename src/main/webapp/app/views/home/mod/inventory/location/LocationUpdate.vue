<template>
  <FormDialog
    :header="$t('entity.home.createOrEditLabel', !location?.id ? 1 : 2, { named: { entity: $t('location.detail.title') } })"
    :validate="validator.location.$invalid"
    @accept="saveCatalog"
    @close="goBack"
  >
    <div class="formgrid grid pt-2">
      <div class="field col-12 md:col-3">
        <FormGroupInput
          id="field_code"
          v-model="location.code"
          :label="$t('location.code')"
          :rules="validator.location.code"
          name="field_code">
        </FormGroupInput>
      </div>
      <div class="field col-12 md:col-9">
        <FormGroupInput
          id="field_name"
          v-model="location.name"
          :label="$t('location.name')"
          :rules="validator.location.name"
          name="field_name">
        </FormGroupInput>
      </div>
      <div class="field col-12 md:col-3">
        <FormGroupDropdown
          id="field_locationType"
          v-model="location.locationType"
          :label="$t('location.locationType')"
          :options="getLocations()"
          :rules="validator.location.locationType"
          name="field_locationType">
        </FormGroupDropdown>
      </div>
      <div class="field col-12 md:col-9">
        <FormGroupInput
          id="field_address"
          v-model="location.address"
          :label="$t('location.address')"
          name="field_address">
        </FormGroupInput>
      </div>
    </div>
  </FormDialog>
</template>

<script lang="ts">
import {defineComponent, reactive, toRaw, watch} from 'vue'
import {useVuelidate} from '@vuelidate/core'
import {RouteLocationNormalized, useRoute, useRouter} from 'vue-router'
import FormDialog from '@/shared/components/ui/form/FormDialog.vue'
import FormGroupInputSwitch from '@/shared/components/ui/form/FormGroupInputSwitch.vue'
import FormGroupInput from '@/shared/components/ui/form/FormGroupInput.vue'
import FormGroupDropdown from '@/shared/components/ui/form/FormGroupDropdown.vue'
import LocationService from '@/views/home/mod/inventory/location/location-service'
import {ILocation, Location} from '@/views/home/mod/inventory/location/location-model'
import locationRules from '@/views/home/mod/inventory/location/location-rules'
import {useCustomToast} from '@/shared/composables/custom-toast'
import {useI18n} from 'vue-i18n'
import {LocationType} from '@/shared/const/entity-constants'

export default defineComponent({
  name: 'LocationUpdate',
  components: {
    FormDialog,
    FormGroupInput,
    FormGroupInputSwitch,
    FormGroupDropdown,
  },
  beforeRouteEnter(to: RouteLocationNormalized, from: RouteLocationNormalized, next) {
    next((vm: any) => {
      if (to.params.id) {
        vm.loadLocation(String(to.params.id))
      }
    })
  },
  setup: () => {
    const route = useRoute()
    const {t} = useI18n()
    const router = useRouter()
    const location: ILocation = reactive(new Location())
    const validator = useVuelidate(locationRules, {location})
    const customToast = useCustomToast()

    const loadLocation = async (login: string): Promise<void> => {
      const {data} = await LocationService.find(login)
      if (data) {
        Object.assign(location, data)
      }
    }

    watch(() => location.locationType, (val: LocationType) => {
      location.isWarehouse = val == LocationType.WAREHOUSE
    }, {immediate: true})

    const goBack = (): void => {
      router.back()
    }

    const getLocations = () => Object.keys(LocationType)

    const saveCatalog = (): void => {
      if (route.params.id && location.id) {
        LocationService.update(toRaw(location), location.id)
          .then(() => {
            customToast.successToast(t('location.actions.update.success'))
            goBack()
          })
          .catch(() => {
            customToast.errorToast(t('location.actions.update.error'))
            goBack()
          })
      } else {
        LocationService.save(toRaw(location))
          .then(() => {
            customToast.successToast(t('location.actions.save.success'))
            goBack()
          })
          .catch(() => {
            customToast.errorToast(t('location.actions.save.error'))
            goBack()
          })
      }
    }

    return {
      location,
      validator,
      getLocations,
      goBack,
      saveCatalog,
      loadLocation,
    }
  },
})
</script>
