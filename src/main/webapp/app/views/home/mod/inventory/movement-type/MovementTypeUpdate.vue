<template>
  <FormDialog
    :header="$t('entity.home.createOrEditLabel', !movementType?.id ? 1 : 2, { named: { entity: $t('movementType.detail.title') } })"
    :validate="validator.movementType.$invalid"
    @accept="saveCatalog"
    @close="goBack"
  >
    <div class="formgrid grid pt-2">
      <div class="field col-3 md:col-3">
        <FormGroupInput
          id="field_code"
          v-model="movementType.code"
          :label="$t('movementType.code')"
          :rules="validator.movementType.code"
          name="field_code"
          :uppercase="true"
        />
      </div>
      <div class="field col-9 md:col-9">
        <FormGroupInput
          id="field_name"
          v-model="movementType.name"
          :label="$t('movementType.name')"
          :rules="validator.movementType.name"
          name="field_name"
        />
      </div>
      <div class="field col-6 md:col-3">
        <FormGroupDropdown
          id="field_operationType"
          v-model="movementType.type"
          :label="$t('movementType.operationType')"
          :options="getOperationType()"
          :rules="validator.movementType.type"
          name="field_operationType"
        />
      </div>
      <div class="field col-6 md:col-3">
        <FormGroupDropdown
          id="field_counterpart"
          v-model="movementType.counterpart"
          :label="$t('movementType.counterpart')"
          :options="getCounterpart()"
          :rules="validator.movementType.counterpart"
          name="field_counterpart"
        />
      </div>
      <div class="field col-12 md:col-6">
        <FormGroupDropdown
          id="field_opposite"
          v-model="movementType.oppositeMovementType"
          :disabled="movementType.type == 'IN'"
          :label="$t('movementType.oppositeMovementType')"
          :options="opposites"
          name="field_opposite"
          optionLabel="codeName"
          showClear
        />
      </div>
    </div>
  </FormDialog>
</template>

<script lang="ts">
import { defineComponent, reactive, toRaw, watch } from 'vue'
import { useVuelidate } from '@vuelidate/core'
import { RouteLocationNormalized, useRoute, useRouter } from 'vue-router'
import FormDialog from '@/shared/components/ui/form/FormDialog.vue'
import FormGroupInput from '@/shared/components/ui/form/FormGroupInput.vue'
import MovementTypeService from '@/views/home/mod/inventory/movement-type/movement-type-service'
import { IMovementType, MovementType } from '@/views/home/mod/inventory/movement-type/movement-type-model'
import movementTypeRules from '@/views/home/mod/inventory/movement-type/movement-type-rules'
import { useCustomToast } from '@/shared/composables/custom-toast'
import { useI18n } from 'vue-i18n'
import { OperationCounterpart, OperationType } from '@/shared/const/entity-constants'
import FormGroupDropdown from '@/shared/components/ui/form/FormGroupDropdown.vue'

export default defineComponent({
  name: 'MovementTypeUpdate',
  components: {
    FormDialog,
    FormGroupInput,
    FormGroupDropdown,
  },
  beforeRouteEnter(to: RouteLocationNormalized, from: RouteLocationNormalized, next) {
    next((vm: any) => {
      if (to.params.id) {
        vm.loadMovementType(String(to.params.id))
      }
    })
  },
  setup: () => {
    const route = useRoute()
    const { t } = useI18n()
    const router = useRouter()
    const movementType: IMovementType = reactive(new MovementType())
    const validator = useVuelidate(movementTypeRules, { movementType })
    const customToast = useCustomToast()
    const opposites: IMovementType[] = reactive([])

    const loadMovementType = async (id: number): Promise<void> => {
      const { data } = await MovementTypeService.find(id)
      if (data) {
        Object.assign(movementType, data)
      }
    }

    watch(
      () => movementType.type,
      (val: OperationType) => {
        if (val == OperationType.IN) {
          movementType.oppositeMovementType = null
        }
      }
    )

    const goBack = (): void => {
      router.back()
    }

    const getOperationType = () => Object.keys(OperationType)

    const getCounterpart = () => Object.keys(OperationCounterpart)

    MovementTypeService.listBasic(OperationType.IN).then(res => {
      const result = res.data.map(item => new MovementType(item.id, item.code, item.name, `${item.code}-${item.name}`))
      Object.assign(opposites, result)
      // Object.assign(opposites, res.data)
    })

    const saveCatalog = (): void => {
      if (route.params.id && movementType.id) {
        MovementTypeService.update(toRaw(movementType), movementType.id)
          .then(() => {
            customToast.successToast(t('movementType.actions.update.success'))
            goBack()
          })
          .catch(() => {
            customToast.errorToast(t('movementType.actions.update.error'))
            goBack()
          })
      } else {
        MovementTypeService.save(toRaw(movementType))
          .then(() => {
            customToast.successToast(t('movementType.actions.save.success'))
            goBack()
          })
          .catch(() => {
            customToast.errorToast(t('movementType.actions.save.error'))
            goBack()
          })
      }
    }

    return {
      movementType: movementType,
      validator,
      goBack,
      saveCatalog,
      loadMovementType,
      getOperationType,
      getCounterpart,
      opposites,
    }
  },
})
</script>
