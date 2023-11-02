<template>
  <!--  We add this for /new /:id/edit /:id/delete child routes-->
  <router-view />
  <DataTableList :entity="taxAreaCodeEntity" :service="TaxAreaCodeService" />
</template>

<script lang="ts" setup>
import DataTableList from '@/shared/components/ui/datatable/DataTableList.vue'
import { taxAreaCodeEntity } from './tax-area-code-entity'
import TaxAreaCodeService from './tax-area-code-service'
</script>
