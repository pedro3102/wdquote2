<template>
  <!--  We add this for /new /:id/edit /:id/delete child routes-->
  <router-view />
  <DataTableList :entity="movementTypeEntity" :service="MovementTypeService" />
</template>

<script lang="ts" setup>
import DataTableList from '@/shared/components/ui/datatable/DataTableList.vue'
import { movementTypeEntity } from './movement-type-entity'
import MovementTypeService from './movement-type-service'
</script>
