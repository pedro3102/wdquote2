<template>
  <!--  We add this for /new /:id/edit /:id/delete child routes-->
  <router-view />
  <DataTableList :entity="systemEntity" :service="SystemService" />
</template>

<script lang="ts" setup>
import DataTableList from '@/shared/components/ui/datatable/DataTableList.vue'
import { systemEntity } from './system-entity'
import SystemService from './system-service'
</script>
