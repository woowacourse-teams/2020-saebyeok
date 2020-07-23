<template>
  <div>
    <my-page-tabs></my-page-tabs>
  </div>
</template>

<script>
import MyPageTabs from '@/components/MyPageTabs.vue';

export default {
  components: {
    MyPageTabs
  },
  props: {
    source: String
  }
};
</script>
