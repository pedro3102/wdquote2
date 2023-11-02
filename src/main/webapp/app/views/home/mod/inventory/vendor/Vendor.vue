<template>
  <!--  We add this for /new /:id/edit /:id/delete child routes-->
  <router-view />
  <DataTableList :entity="vendorEntity" :service="VendorService" />
</template>

<script lang="ts" setup>
import DataTableList from '@/shared/components/ui/datatable/DataTableList.vue'
import { vendorEntity } from './vendor-entity'
import VendorService from './vendor-service'
</script>
