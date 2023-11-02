<template>
  <div class="error-page">
    <h2 class="headline text-warning">403</h2>
    <div class="error-content">
      <h3>
        <em class="fas fa-exclamation-triangle text-warning" />
        {{ $t('error.notFound.title') }}
      </h3>
      <p>
        {{ $t('error.notFound.description') }}
        <router-link :to="{ name: `${dashboardRouteName}` }">
          {{ $t('error.returnToDashBoard') }} </router-link
        >.
      </p>
    </div>
    <!-- /.error-content -->
  </div>
</template>

<script>
import { dashboardRouteName } from '../dashboard/routes';

export default {
  name: 'NotFound',
  setup() {
    return {
      dashboardRouteName,
    };
  },
};
</script>
