<template>
  <BaseButton @click="toggle" text>
    <font-awesome-icon icon="bars" />
  </BaseButton>
  <Menu id="overlay_menu" ref="menu" :model="entity.rowActions" :popup="true" style="width: auto">
    <template #item="{ item: action }">
      <RenderActions :action="action" :row="row" :entity="entity" @custom="emitCustom" @remove="emitRemove" icon-class="px-2" />
    </template>
  </Menu>
</template>

<script setup lang="ts">
import { ref, PropType } from 'vue'
import Menu from 'primevue/menu'
import BaseButton from '@/shared/components/ui/buttons/BaseButton.vue'
import { useRouter } from 'vue-router'
import { IEntity } from '@/views/entities/common/entity-interface'
import RenderActions from './RenderActions'

const router = useRouter()

const emit = defineEmits(['custom', 'remove'])
defineProps({
  entity: {
    type: Object as PropType<IEntity>,
    required: true,
  },
  row: {
    type: Object,
    default: () => {},
  },
})
const menu = ref()

const toggle = event => {
  menu.value.toggle(event)
}

const emitCustom = info => {
  emit('custom', info)
}

const emitRemove = info => {
  emit('remove', info)
}
</script>
