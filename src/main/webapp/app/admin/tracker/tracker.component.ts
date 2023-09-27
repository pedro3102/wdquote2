import { defineComponent, inject, ref, Ref, onMounted, onUnmounted } from 'vue';
import { useI18n } from 'vue-i18n';
import { Subscription } from 'rxjs';

import { useRoute } from 'vue-router';
import TrackerService from './tracker.service';
import { useDateFormat } from '@/shared/composables';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'JhiTrackerComponent',
  setup() {
    const { formatDateShort: formatDate } = useDateFormat();
    const trackerService = inject<TrackerService>('trackerService');
    const activities: Ref<any[]> = ref([]);
    const route = useRoute();
    let subscription: Subscription;

    const showActivity = (activity: any) => {
      let existingActivity = false;
      for (let index = 0; index < activities.value.length; index++) {
        if (activities.value[index].sessionId === activity.sessionId) {
          existingActivity = true;
          if (activity.page === 'logout') {
            activities.value.splice(index, 1);
          } else {
            activities.value.splice(index, 1);
            activities.value.push(activity);
          }
        }
      }
      if (!existingActivity && activity.page !== 'logout') {
        activities.value.push(activity);
      }
    };

    onMounted(() => {
      subscription = trackerService.subscribe(activity => {
        showActivity(activity);
      });
      // Make sure current session shows on the list
      trackerService.sendActivity(route.fullPath);
    });

    onUnmounted(() => {
      if (subscription) {
        subscription.unsubscribe();
        subscription = null;
      }
    });

    return {
      activities,
      formatDate,
      t$: useI18n().t,
    };
  },
});
