<template>
  <ConfirmDialog :group="group" :pt="{ headertitle: 'mr-4' }">
    <template #message="slotProps">
      <div class="flex p-4">
        <font-awesome-icon icon="circle-question" style="font-size: 1.5rem" />
        <p class="pl-2">{{ slotProps.message.message }}</p>
      </div>
    </template>
    <template #accepticon>
      <font-awesome-icon icon="check" class="m-1" />
    </template>
    <template #rejecticon>
      <font-awesome-icon icon="times" class="m-1" />
    </template>
  </ConfirmDialog>
</template>

<script setup>
import ConfirmDialog from 'primevue/confirmdialog'

defineProps({
  group: {
    type: String,
    required: true,
  },
})

const emit = defineEmits(['accept', 'reject'])
</script>
