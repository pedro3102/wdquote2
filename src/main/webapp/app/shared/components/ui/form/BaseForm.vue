<template>
  <form method="post" v-bind="$attrs">
    <slot />
  </form>
</template>

<script>
export default {
  name: 'BaseForm',
}
</script>
