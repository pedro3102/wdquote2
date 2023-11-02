<template>
  <FormDialog
    @close="goBack"
    @accept="saveCatalog"
    :header="$t('entity.home.createOrEditLabel', !product?.id ? 1 : 2, { named: { entity: $t('product.detail.title') } })"
    :validate="validator.product.$invalid"
  >
    <div class="formgrid grid pt-2">
      <div class="field col-12 md:col-6">
        <FormGroupInput
          id="field_code"
          v-model="product.code"
          name="field_code"
          :label="$t('product.code')"
          :rules="validator.product.code"
        >
        </FormGroupInput>
      </div>
      <div class="field col-12 md:col-6">
        <FormGroupInput
          id="field_description"
          v-model="product.description"
          name="field_description"
          :label="$t('product.description')"
          :rules="validator.product.description"
        >
        </FormGroupInput>
      </div>
      <div class="field col-12 md:col-6">
        <FormGroupDropdown
          id="field_category"
          v-model="product.category"
          name="field_category"
          :label="$t('product.category')"
          :options="categories"
          optionLabel="name"
          :rules="validator.product.category"
        >
        </FormGroupDropdown>
      </div>
      <div class="field col-12 md:col-6">
        <FormGroupDropdown
          id="field_uom"
          v-model="product.uom"
          name="field_uom"
          :label="$t('product.uom')"
          :options="uoms"
          optionLabel="name"
          :rules="validator.product.uom"
        >
        </FormGroupDropdown>
      </div>
      <div class="field col-12 md:col-6">
        <FormGroupDropdown
          id="field_uomWeight"
          v-model="product.uomWeight"
          name="field_uomWeight"
          :label="$t('product.uomWeight')"
          :options="uoms"
          optionLabel="name"
          :rules="validator.product.uomWeight"
          showClear
        >
        </FormGroupDropdown>
      </div>
      <div class="field col-12 md:col-6">
        <FormGroupNumber
          id="field_weight"
          v-model="product.weight"
          name="field_weight"
          :maxFractionDigits="2"
          :label="$t('product.weight')"
          :rules="validator.product.weight"
        >
          <template #customErrors v-if="validator.product?.weight?.positive?.$invalid">
            <small class="invalid-feedback" v-text="$t('entity.validation.positive')" />
          </template>
        </FormGroupNumber>
      </div>
      <div class="field col-12 md:col-12">
        <FormGroupMultiSelect
          id="field_systemModels"
          v-model="product.systemModels"
          name="field_systemModels"
          :label="$t('product.systemModel')"
          optionLabel="name"
          :options="systemModels"
        >
        </FormGroupMultiSelect>
      </div>
    </div>
  </FormDialog>
</template>

<script lang="ts">
import { defineComponent, reactive, toRaw } from 'vue'
import { useVuelidate } from '@vuelidate/core'
import { RouteLocationNormalized, useRoute, useRouter } from 'vue-router'
import FormDialog from '@/shared/components/ui/form/FormDialog.vue'
import FormGroupInput from '@/shared/components/ui/form/FormGroupInput.vue'
import FormGroupDropdown from '@/shared/components/ui/form/FormGroupDropdown.vue'
import FormGroupMultiSelect from '@/shared/components/ui/form/FormGroupMultiSelect.vue'
import FormGroupNumber from '@/shared/components/ui/form/FormGroupNumber.vue'
import ProductService from '@/views/home/mod/inventory/product/product-service'
import { IProduct, Product } from '@/views/home/mod/inventory/product/product-model'
import { useCustomToast } from '@/shared/composables/custom-toast'
import { useI18n } from 'vue-i18n'
import { IProductCategory } from '@/views/home/mod/inventory/product-category/product-category-model'
import { IUnitOfMeasure } from '@/views/home/mod/inventory/unit-of-measure/unit-of-measure-model'
import UnitOfMeasureService from '@/views/home/mod/inventory/unit-of-measure/unit-of-measure-service'
import ProductCategoryService from '@/views/home/mod/inventory/product-category/product-category-service'
import { ISystemModel } from '@/views/home/mod/inventory/system-model/system-model-model'
import SystemModelService from '@/views/home/mod/inventory/system-model/system-model-service'
import { maxLength, required, requiredIf } from '@vuelidate/validators'

export default defineComponent({
  name: 'ProductUpdate',
  components: {
    FormDialog,
    FormGroupInput,
    FormGroupDropdown,
    FormGroupMultiSelect,
    FormGroupNumber,
  },
  beforeRouteEnter(to: RouteLocationNormalized, from: RouteLocationNormalized, next) {
    next((vm: any) => {
      if (to.params.id) {
        vm.loadProduct(String(to.params.id))
      }
    })
  },
  setup: () => {
    const weightValidator = (value: number) => {
      if (value != 0 && !value) {
        return true
      }
      return value > 0
    }
    const productRules: any = {
      product: {
        code: {
          required,
          maxLength: maxLength(50),
        },
        description: {
          required,
          maxLength: maxLength(255),
        },
        category: {
          required,
        },
        uom: {
          required,
        },
        uomWeight: {
          required: requiredIf(() => product.weight),
        },
        weight: {
          positive: weightValidator,
          required: requiredIf(() => product.uomWeight),
        },
      },
    }

    const route = useRoute()
    const { t } = useI18n()
    const router = useRouter()
    const product: IProduct = reactive(new Product())
    const validator = useVuelidate(productRules, { product })
    const customToast = useCustomToast()
    const categories: IProductCategory[] = reactive([])
    const uoms: IUnitOfMeasure[] = reactive([])
    const systemModels: ISystemModel[] = reactive([])

    const loadProduct = async (login: string): Promise<void> => {
      const { data } = await ProductService.find(login)
      if (data) {
        Object.assign(product, data)
      }
    }

    const goBack = (): void => {
      router.back()
    }

    ProductCategoryService.listBasic().then(res => {
      Object.assign(categories, res.data)
    })

    UnitOfMeasureService.list(UnitOfMeasureService.baseUrl).then(res => {
      Object.assign(uoms, res.data)
    })

    SystemModelService.listBasic().then(res => {
      Object.assign(systemModels, res.data)
    })

    const saveCatalog = (): void => {
      if (route.params.id && product.id) {
        console.log(product)
        ProductService.update(toRaw(product), product.id)
          .then(() => {
            customToast.successToast(t('product.actions.update.success'))
            goBack()
          })
          .catch(() => {
            customToast.errorToast(t('product.actions.update.error'))
            goBack()
          })
      } else {
        ProductService.save(toRaw(product))
          .then(() => {
            customToast.successToast(t('product.actions.save.success'))
            goBack()
          })
          .catch(() => {
            customToast.errorToast(t('product.actions.save.error'))
            goBack()
          })
      }
    }

    return {
      product,
      validator,
      categories,
      uoms,
      systemModels,
      goBack,
      saveCatalog,
      loadProduct,
    }
  },
})
</script>
