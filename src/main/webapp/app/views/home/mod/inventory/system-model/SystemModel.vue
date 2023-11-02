<template>
  <!--  We add this for /new /:id/edit /:id/delete child routes-->
  <router-view />
  <DataTableList :entity="systemModelEntity" :service="SystemModelService" />
</template>

<script lang="ts" setup>
import DataTableList from '@/shared/components/ui/datatable/DataTableList.vue'
import { systemModelEntity } from './system-model-entity'
import SystemModelService from './system-model-service'
</script>
