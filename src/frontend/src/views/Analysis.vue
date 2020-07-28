<template>
  <div>
    <my-page-tabs></my-page-tabs>
    <v-card class="mx-auto" color="#faf9f5" max-width="400">
      <v-card-title>감정 그래프</v-card-title>~
    </v-card>
    <chartjs-radar
      :labels="labels"
      :data="dataset"
      :bind="true"
    ></chartjs-radar>
  </div>
</template>

<script src="//unpkg.com/hchs-vue-charts@1.2.8"></script>
<script>
import MyPageTabs from '@/components/MyPageTabs.vue';

export default {
  data() {
    return {
      dataentry: null,
      datalabel: null,
      labels: [
        'January',
        'February',
        'March',
        'April',
        'May',
        'June',
        'July',
        'August',
        'September',
        'October',
        'November',
        'December'
      ],
      dataset: [5, 10, 15, 25, 45, 70, 115, 185, 70, 75, 70, 60]
    };
  },
  components: {
    MyPageTabs
  },
  props: {
    source: String
  }
};
</script>
