<template>
  <!-- Content Wrapper. Contains page content -->
  <div class="layout-main">
    <ContentHeader />
    <Content />
  </div>
  <!-- /.content-wrapper -->
</template>

<script>
import ContentHeader from './ContentHeader.vue'
import Content from './Content.vue'

export default {
  name: 'ContentWrapper',
  components: {
    Content,
    ContentHeader,
  },
}
</script>

<style>
.layout-main-container {
  padding: 6rem 2rem 2rem 4rem;
}
</style>
