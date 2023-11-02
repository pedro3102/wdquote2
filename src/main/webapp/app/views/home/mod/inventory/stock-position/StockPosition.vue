<template>
  <!--  We add this for /new /:id/edit /:id/delete child routes-->
  <router-view/>
  <DataTableList :entity="stockPositionEntity" :service="StockPositionService"/>
</template>

<script lang="ts" setup>
import DataTableList from '@/shared/components/ui/datatable/DataTableList.vue'
import {stockPositionEntity} from './stock-position-entity'
import StockPositionService from './stock-position-service'</script>
