<template>
  <Dropdown
    class="remove-dropdown-trigger"
    v-model="currentLanguage"
    :options="languages"
    optionLabel="key"
    valueLabel="key"
    @change="setLanguage"
  >
    <template #value="slotProps">
      <div v-if="slotProps.value" class="flex align-items-center">
        <img :alt="slotProps.value" :src="`/content/images/layout/${slotProps.value}.svg`" style="width: 25px" />
      </div>
      <span v-else>
        <img :alt="slotProps.option" src="/content/images/layout/en.svg" style="width: 25px" />
      </span>
    </template>
    <template #option="slotProps">
      <div class="flex align-items-center">
        <img class="" :alt="slotProps.option" :src="`/content/images/layout/${slotProps.option}.svg`" style="width: 25px" />
      </div>
    </template>
  </Dropdown>
</template>

<script setup lang="ts">
import { computed, inject, onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import Dropdown from 'primevue/dropdown'
import TranslationService from '@/locale/translation-service'
import { useLangStore } from '@/store/modules/i18n/translation-store'

const route = useRoute()
const langStore = useLangStore()
const translationService = inject<TranslationService>('translationService')

const currentLanguage = ref()
const languages = computed<string[]>(() => {
  const langOptions = []
  langStore.getLanguages.forEach(lang => {
    if (lang.key !== currentLanguage.value) {
      langOptions.push(lang.key)
    }
  })
  return langOptions
})
const loadLanguage = () => {
  route.matched.forEach(match => {
    const { lang }: any = match.meta
    translationService?.loadLanguageAsync(lang?.length ? lang : [])
  })
}

const setLanguage = () => {
  langStore.setCurrentLanguage(currentLanguage.value).then(() => {
    loadLanguage()
  })
}
onMounted(() => {
  currentLanguage.value = langStore.getCurrentLanguage
})
</script>

<style lang="scss">
.remove-dropdown-trigger {
  border: 0;
  margin-left: 1rem;
  .p-dropdown-trigger {
    display: none;
  }
  .p-dropdown-panel {
    padding: 0 !important;
  }
}
</style>
