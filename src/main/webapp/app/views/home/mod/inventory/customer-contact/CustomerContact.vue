<template>
  <!--  We add this for /new /:id/edit /:id/delete child routes-->
  <router-view />
  <DataTableList :entity="customerContactEntity" :service="CustomerContactService" />
</template>

<script lang="ts" setup>
import DataTableList from '@/shared/components/ui/datatable/DataTableList.vue'
import { customerContactEntity } from './customer-contact-entity'
import CustomerContactService from './customer-contact-service'
</script>
