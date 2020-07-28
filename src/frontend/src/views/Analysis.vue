<template>
  <div>
    <my-page-tabs></my-page-tabs>
    <v-card class="mx-auto" color="#faf9f5" max-width="400">
      <v-card-title>감정 그래프</v-card-title>
      <radar-chart />
    </v-card>
  </div>
</template>

<script>
import MyPageTabs from '@/components/MyPageTabs.vue';
import RadarChart from '@/components/RadarChart.vue';
export default {
  data() {
    return {};
  },
  components: {
    MyPageTabs,
    RadarChart
  },
  props: {
    source: String
  }
};
</script>
