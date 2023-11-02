<template>
  <!--  We add this for /new /:id/edit /:id/delete child routes-->
  <router-view />
  <DataTableList :entity="languageEntity" :service="LanguageService" />
</template>

<script lang="ts" setup>
import DataTableList from '@/shared/components/ui/datatable/DataTableList.vue'
import { languageEntity } from './language-entity'
import LanguageService from './language-service'
</script>
