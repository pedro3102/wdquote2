<template>
  <FormDialog
    @close="goBack"
    @accept="saveCatalog"
    :header="$t('entity.home.createOrEditLabel', !productCategory?.id ? 1 : 2, { named: { entity: $t('productCategory.detail.title') } })"
    :validate="validator.productCategory.$invalid"
  >
    <div class="formgrid grid pt-2">
      <div class="field col-12 md:col-12">
        <FormGroupInput
          id="field_name"
          v-model="productCategory.name"
          name="field_name"
          :label="$t('productCategory.name')"
          :rules="validator.productCategory.name"
        >
        </FormGroupInput>
      </div>
    </div>
  </FormDialog>
</template>

<script lang="ts">
import { defineComponent, reactive, ref, Ref, toRaw } from 'vue'
import { useVuelidate } from '@vuelidate/core'
import { RouteLocationNormalized, useRoute, useRouter } from 'vue-router'
import FormDialog from '@/shared/components/ui/form/FormDialog.vue'
import FormGroupInputSwitch from '@/shared/components/ui/form/FormGroupInputSwitch.vue'
import FormGroupInput from '@/shared/components/ui/form/FormGroupInput.vue'
import FormGroupDropdown from '@/shared/components/ui/form/FormGroupDropdown.vue'
import ProductCategoryService from '@/views/home/mod/inventory/product-category/product-category-service'
import { IProductCategory, ProductCategory } from '@/views/home/mod/inventory/product-category/product-category-model'
import productCategoryRules from '@/views/home/mod/inventory/product-category/product-category-rules'
import { useLangStore } from '@/store/modules/i18n/translation-store'
import { useCustomToast } from '@/shared/composables/custom-toast'
import { useI18n } from 'vue-i18n'

export default defineComponent({
  name: 'ProductCategoryUpdate',
  components: {
    FormDialog,
    FormGroupInput,
    FormGroupInputSwitch,
    FormGroupDropdown,
  },
  beforeRouteEnter(to: RouteLocationNormalized, from: RouteLocationNormalized, next) {
    next((vm: any) => {
      if (to.params.id) {
        vm.loadProductCategory(String(to.params.id))
      }
    })
  },
  setup: () => {
    const route = useRoute()
    const { t } = useI18n()
    const router = useRouter()
    const productCategory: IProductCategory = reactive(new ProductCategory())
    const validator = useVuelidate(productCategoryRules, { productCategory })
    const customToast = useCustomToast()

    const loadProductCategory = async (id: number): Promise<void> => {
      const { data } = await ProductCategoryService.find(id)
      if (data) {
        Object.assign(productCategory, data)
      }
    }

    const goBack = (): void => {
      router.back()
    }

    const saveCatalog = (): void => {
      if (route.params.id && productCategory.id) {
        ProductCategoryService.update(toRaw(productCategory), productCategory.id)
          .then(() => {
            customToast.successToast(t('productCategory.actions.update.success'))
            goBack()
          })
          .catch(() => {
            customToast.errorToast(t('productCategory.actions.update.error'))
            goBack()
          })
      } else {
        ProductCategoryService.save(toRaw(productCategory))
          .then(() => {
            customToast.successToast(t('productCategory.actions.save.success'))
            goBack()
          })
          .catch(() => {
            customToast.errorToast(t('productCategory.actions.save.error'))
            goBack()
          })
      }
    }

    return {
      productCategory: productCategory,
      validator,
      goBack,
      saveCatalog,
      loadProductCategory,
    }
  },
})
</script>
