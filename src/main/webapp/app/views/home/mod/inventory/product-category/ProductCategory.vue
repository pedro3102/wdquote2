<template>
  <!--  We add this for /new /:id/edit /:id/delete child routes-->
  <router-view />
  <DataTableList :entity="productCategoryEntity" :service="ProductCategoryService" />
</template>

<script lang="ts" setup>
import DataTableList from '@/shared/components/ui/datatable/DataTableList.vue'
import { productCategoryEntity } from './product-category-entity'
import ProductCategoryService from './product-category-service'
</script>
