<template>
  <BaseButton
    v-if="isLinkVisible(action)"
    v-tooltip.top="$t(action.title())"
    :class="action.class"
    @click="navigateToLink(action)"
    :size="sizeProperty"
    :text="textProperty"
    :raised="raisedProperty"
  >
    <font-awesome-icon :class="iconClass" :icon="action.icon()" :color="action.iconColor()" />
    <!--    <span v-if="printLabel">{{ $t(action.label()) }}</span>-->
  </BaseButton>
  <BaseButton
    v-if="isButtonVisible(action)"
    v-tooltip.top="$t(action.title())"
    :class="action.class"
    @click="executeAction(action)"
    :size="sizeProperty"
    :text="textProperty"
    :raised="raisedProperty"
  >
    <font-awesome-icon :class="iconClass" :icon="action.icon()" :color="action.iconColor()" />
    <!--    <span v-if="printLabel">{{ $t(action.label()) }}</span>-->
  </BaseButton>
  <BaseButton
    v-if="isEmitterVisible(action)"
    v-tooltip.top="$t(action.title())"
    :class="action.class"
    @click="executeAction(action)"
    :size="sizeProperty"
    :text="textProperty"
    :raised="raisedProperty"
  >
    <font-awesome-icon :class="iconClass" :icon="action.icon()" :color="action.iconColor()" />
    <!--    <span v-if="printLabel">{{ $t(action.label()) }}</span>-->
  </BaseButton>
</template>

<script setup lang="ts">
import { isLinkVisible, isButtonVisible, isActionVisible } from '@/views/entities/common/entity-api'
import BaseButton from '@/shared/components/ui/buttons/BaseButton.vue'
import { useRouter } from 'vue-router'
import { IAction, IEntity } from '@/views/entities/common/entity-interface'
import { PropType } from 'vue'
import { ActionType } from '@/shared/const/entity-constants'

const emit = defineEmits(['custom', 'remove'])
const props = defineProps({
  entity: {
    type: Object as PropType<IEntity>,
    required: true,
  },
  row: {
    type: Object,
    default: () => {},
    required: true,
  },
  action: {
    type: Object as PropType<IAction>,
    default: () => {},
    required: true,
  },
  textProperty: {
    type: Boolean,
    default: true,
  },
  raisedProperty: {
    type: Boolean,
    default: false,
  },
  sizeProperty: {
    type: String,
    default: '',
  },
  printLabel: {
    type: Boolean,
    default: true,
  },
  iconClass: {
    type: String,
    default: '',
  },
})

const router = useRouter()

const navigateToLink = (action: IAction) => {
  router.push(action.execute(props.row, props.entity.findParamProp || 'id'))
}

/**
 * Emit custom event with te selected element.
 * @returns {Object} The row object.
 */
const executeAction = (action: IAction): void => {
  if (action.type === ActionType.EMITTER) {
    emit('custom', action.id)
    return
  }
  if (action?.execute) action.execute(props.row, emit)
}

/**
 * Return if the action is an Emitter and is visible.
 * @param action The action
 * @param row The row data
 */
const isEmitterVisible = (action: IAction, row?: object) => action.type === ActionType.EMITTER && isActionVisible(action, row)
</script>
