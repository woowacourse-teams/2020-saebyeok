<template>
  <div>
    <my-page-tabs></my-page-tabs>
    <v-card
      class="mx-auto rounded-lg"
      color="#faf9f5"
      max-width="400"
      style="padding: 12px; margin: 12px"
    >
      <v-card-title>감정 그래프</v-card-title>
      <radar-chart />
      <v-spacer style="height: 20px" />
    </v-card>
    <v-card
      class="mx-auto rounded-lg"
      color="#faf9f5"
      max-width="400"
      style="padding: 12px; margin: 12px"
    >
      <v-card-title>지난 1달간 당신은 <br />대체로 행복 했습니다.</v-card-title>
    </v-card>
    <v-card
      class="mx-auto rounded-lg"
      color="#faf9f5"
      max-width="400"
      style="padding: 12px; margin: 12px"
    >
      <div id="comment-statistics">
        댓글 작성수 <b>42</b> | 댓글 추천수 <b>123</b>
      </div>
    </v-card>
  </div>
</template>

<script>
import MyPageTabs from '@/components/MyPageTabs.vue';
import RadarChart from './components/RadarChart.vue';

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
