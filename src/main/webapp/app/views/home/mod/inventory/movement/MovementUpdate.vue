<template>
  <FormDialog
    :header="$t('entity.home.createOrEditLabel', !movement?.id ? 1 : 2, { named: { entity: $t('movement.detail.title') } })"
    :validate="validator.movement.$invalid"
    @accept="saveCatalog"
    @close="goBack">
    <div class="formgrid grid pt-2">
      <div class="field col-12 md:col-6">
        <FormGroupDropdown
          id="field_movementType"
          v-model="movementType"
          :label="$t('movement.movementType')"
          :options="movementTypes"
          :rules="validator.movement.movementType"
          name="field_movementType"
          optionLabel="codeName"
          @change="movementTypeChange"
        >
        </FormGroupDropdown>
      </div>
      <div class="field col-12 md:col-6">
        <FormGroupDropdown
          id="field_location"
          v-model="location"
          :label="$t('movement.location')"
          :options="locations"
          :rules="validator.movement.location"
          class="pr-2"
          name="field_location"
          optionLabel="codeName"
          @change="locationChange">
        </FormGroupDropdown>
      </div>
      <div class="field col-6 md:col-3">
        <FormGroupCalendar
          id="field_date"
          v-model="movement.date"
          :label="$t('movement.date')"
          :rules="validator.movement.date"
          name="field_date">
        </FormGroupCalendar>
      </div>
      <div class="field col-6 md:col-3">
        <FormGroupInput
          id="field_reference"
          v-model="movement.reference"
          :label="$t('movement.reference')"
          :rules="validator.movement.reference"
          name="field_reference">
        </FormGroupInput>
      </div>
      <div class="field col-12 md:col-6">
        <FormGroupDropdown
          id="field_counterpart"
          v-model="movement.counterpartEntity"
          :disabled="movement.movementType == null || movement.movementType.counterpart === 'NONE'"
          :label="$t('movement.counterpart')"
          :options="counterpart.counterparts"
          :rules="validator.movement.counterpartEntity"
          class="pr-2"
          name="field_counterpart"
          optionLabel="codeName">
        </FormGroupDropdown>
      </div>
      <div class="field col-12 md:col-12">
        <FormGroupInput
          id="field_note"
          v-model="movement.note"
          :label="$t('movement.note')"
          :rules="validator.movement.note"
          name="field_note">
        </FormGroupInput>
      </div>
    </div>
  </FormDialog>
</template>

<script lang="ts">
import {defineComponent, reactive, Ref, ref, toRaw, watch} from 'vue'
import {useVuelidate} from '@vuelidate/core'
import {RouteLocationNormalized, useRoute, useRouter} from 'vue-router'
import FormDialog from '@/shared/components/ui/form/FormDialog.vue'
import FormGroupInput from '@/shared/components/ui/form/FormGroupInput.vue'
import MovementService from '@/views/home/mod/inventory/movement/movement-service'
import {IMovement, Movement} from '@/views/home/mod/inventory/movement/movement-model'
import {useCustomToast} from '@/shared/composables/custom-toast'
import {useI18n} from 'vue-i18n'
import {IMovementType} from "@/views/home/mod/inventory/movement-type/movement-type-model";
import MovementTypeService from "@/views/home/mod/inventory/movement-type/movement-type-service";
import {ILocation, Location} from "@/views/home/mod/inventory/location/location-model";
import LocationService from "@/views/home/mod/inventory/location/location-service";
import {OperationCounterpart, OperationType} from "@/shared/const/entity-constants";
import FormGroupDropdown from "@/shared/components/ui/form/FormGroupDropdown.vue";
import FormGroupCalendar from "@/shared/components/ui/form/FormGroupCalendar.vue";
import VendorService from "@/views/home/mod/inventory/vendor/vendor-service";
import CustomerService from "@/views/home/mod/inventory/customer/customer-service";
import {Vendor} from "@/views/home/mod/inventory/vendor/vendor-model";
import {Customer} from "@/views/home/mod/inventory/customer/customer-model";
import {maxLength, required, requiredIf} from "@vuelidate/validators";
import {useCustomConfirm} from "@/shared/composables/custom-confirm";


export default defineComponent({
  name: 'MovementUpdate',
  components: {
    FormGroupCalendar,
    FormGroupDropdown,
    FormDialog,
    FormGroupInput,
  },
  beforeRouteEnter(to: RouteLocationNormalized, from: RouteLocationNormalized, next) {
    next(async (vm: any) => {
      await vm.preloadLocations()
      if (to.params.id) {
        vm.loadMovement(String(to.params.id))
      }
    })
  },
  setup: () => {
    const movementRules: any = {
      movement: {
        reference: {
          maxLength: maxLength(20),
        },
        date: {
          required,
        },
        note: {
          maxLength: maxLength(255),
        },
        movementType: {
          required,
        },
        location: {
          required,
        },
        counterpartEntity: {
          required: requiredIf(() => movement.movementType?.counterpart !== OperationCounterpart.NONE)
        },
      },
    }
    const route = useRoute()
    const {t} = useI18n()
    const router = useRouter()
    const confirm = useCustomConfirm()
    const movement: IMovement = reactive(new Movement())
    const validator = useVuelidate(movementRules, {movement})
    const customToast = useCustomToast()
    const movementTypes: IMovementType[] = reactive([])
    const movementType: Ref<IMovementType> = ref(null)
    const locations: ILocation[] = reactive([])
    const location: Ref<ILocation> = ref(null)
    const counterpart: any = reactive({
      vendors: [],
      customers: [],
      counterparts: [],
    })

    const loadMovement = async (id: number): Promise<void> => {
      const {data} = await MovementService.find(id)
      if (data) {
        Object.assign(movement, data)
        movement.counterpartEntity = movement.counterpartLocation || movement.counterpartVendor || movement.counterpartCustomer;
        movementType.value = movement.movementType
        location.value = movement.location
      }
    }

    const goBack = (): void => {
      router.back()
    }

    const movementTypeChange = (): void => {
      if (movement.id && movement.movementType && movement.movementType.type !== movementType.value.type && movementType.value.type === OperationType.OUT) {
        confirm.openGenericConfirm('general', 'movement.actions.confirm.title',
          'movement.actions.change.movementType',
          async () => {
            if (await deleteDetails()) {
              movement.movementType = movementType.value
            } else {
              movementType.value = movement.movementType
            }
          },
          () => {
            movementType.value = movement.movementType
          })
      } else {
        movement.movementType = movementType.value
      }
    }

    const locationChange = (): void => {
      if (movement.id && movement.location && movement.location !== location.value) {
        confirm.openGenericConfirm('general', 'movement.actions.confirm.title',
          'movement.actions.change.location',
          async () => {
            if (await deleteDetails()) {
              movement.location = location.value
            } else {
              location.value = movement.location
            }
          },
          () => {
            location.value = movement.location
          })
      } else {
        movement.location = location.value
      }
    }

    const deleteDetails = async () => {
      try {
        await MovementService.removeDetail(movement.id);
        return true;
      } catch (error) {
        customToast.responseErrorToast('movement.actions.change.error');
        return false;
      }
    }

    watch(() => movement.movementType, (movementType: IMovementType, oldMovementType: IMovementType) => {
      if (movementType) {
        refreshCounterpart(movementType.counterpart)
        movement.type = movementType.type
        movement.counterpart = movementType.counterpart
        if (oldMovementType) {
          movement.counterpartEntity = null;
        }
      }
    })

    watch(() => movement.location, (location: any, oldLocation: any) => {
      if (movement.counterpart === OperationCounterpart.LOCATION) {
        counterpart.counterparts = locations.filter(l => l.id !== location.id)
        if (oldLocation) {
          movement.counterpartEntity = null
        }
      }
    })

    watch(() => movement.counterpartEntity, (val: any) => {
      movement.counterpartLocation = null
      movement.counterpartVendor = null
      movement.counterpartCustomer = null
      if (val) {
        switch (movement.counterpart) {
          case OperationCounterpart.LOCATION: {
            movement.counterpartLocation = new Location(val.id)
            break;
          }
          case OperationCounterpart.VENDOR: {
            movement.counterpartVendor = new Vendor(val.id)
            break;
          }
          case OperationCounterpart.CUSTOMER: {
            movement.counterpartCustomer = new Customer(val.id)
            break;
          }
        }
      }
    })

    const refreshCounterpart = async (movCounterpart: OperationCounterpart): Promise<void> => {
      switch (movCounterpart) {
        case OperationCounterpart.VENDOR: {
          if (counterpart.vendors.length === 0) {
            await loadVendors()
          }
          counterpart.counterparts = [...counterpart.vendors];
          break;
        }
        case OperationCounterpart.CUSTOMER: {
          if (counterpart.customers.length === 0) {
            await loadCustomers()
          }
          counterpart.counterparts = [...counterpart.customers];
          break;
        }
        case OperationCounterpart.LOCATION: {
          counterpart.counterparts = locations.filter(l => !movement.location || l.id !== movement.location.id)
          break;
        }
      }
    }

    const preloadLocations = async (): Promise<void> => {
      await LocationService.listBasic().then(res => {
        Object.assign(locations, res.data)
      })
    }

    const loadVendors = async (): Promise<void> => {
      await VendorService.listBasic().then(res => {
        Object.assign(counterpart.vendors, res.data)
      })
    }

    const loadCustomers = async (): Promise<void> => {
      await CustomerService.listBasic().then(res => {
        Object.assign(counterpart.customers, res.data)
      })
    }

    MovementTypeService.list(MovementTypeService.baseUrl).then(res => {
      Object.assign(movementTypes, res.data)
    })

    const saveCatalog = (): void => {
      if (route.params.id && movement.id) {
        MovementService.update(toRaw(movement), movement.id)
          .then(() => {
            customToast.successToast(t('movement.actions.update.success'))
            goBack()
          })
          .catch(() => {
            customToast.errorToast(t('movement.actions.update.error'))
            goBack()
          })
      } else {
        MovementService.save(toRaw(movement))
          .then(() => {
            customToast.successToast(t('movement.actions.save.success'))
            goBack()
          })
          .catch(() => {
            customToast.errorToast(t('movement.actions.save.error'))
            goBack()
          })
      }
    }

    return {
      movement,
      validator,
      goBack,
      saveCatalog,
      loadMovement,
      refreshCounterpart,
      movementType,
      movementTypes,
      location,
      locations,
      counterpart,
      preloadLocations,
      loadVendors,
      loadCustomers,
      movementTypeChange,
      locationChange,
      deleteDetails
    }
  },
})
</script>
