<template>
  <!--  We add this for /new /:id/edit /:id/delete child routes-->
  <router-view />
  <DataTableList :entity="deliveryZoneEntity" :service="DeliveryZoneService" />
</template>

<script lang="ts" setup>
import DataTableList from '@/shared/components/ui/datatable/DataTableList.vue'
import { deliveryZoneEntity } from './delivery-zone-entity'
import DeliveryZoneService from './delivery-zone-service'
</script>
