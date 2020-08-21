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
      <radar-chart v-if="loaded" :chartdata="chartdata" :options="options" />
      <v-spacer style="height: 20px" />
    </v-card>
    <v-card
      class="mx-auto rounded-lg"
      color="#faf9f5"
      max-width="400"
      style="padding: 12px; margin: 12px"
    >
      <div class="text-h6" v-html="articlesAnalysisMessage"></div>
    </v-card>
    <v-card
      class="mx-auto rounded-lg"
      color="#faf9f5"
      max-width="400"
      style="padding: 12px; margin: 12px"
    >
      <div id="comment-statistics">
        남겨준 댓글 수 <b>{{ commentsAnalysis.totalCommentsCount }}</b> |
        공감받은 댓글 수
        <b>{{ likedCommentsCount }}</b>
      </div>
    </v-card>
  </div>
</template>

<script>
import MyPageTabs from '@/components/MyPageTabs.vue';
import RadarChart from './components/RadarChart.vue';
import ARTICLES_ANALYSIS_MESSAGES from '@/utils/ArticlesAnalysisMessages.js';
import { mapActions, mapGetters } from 'vuex';
import {
  FETCH_ARTICLES_ANALYSIS,
  FETCH_COMMENTS_ANALYSIS
} from '@/store/shared/actionTypes';

export default {
  name: 'Analysis',
  components: {
    MyPageTabs,
    RadarChart
  },
  data: () => ({
    loaded: false,
    chartdata: null,
    options: null,
    articlesAnalysisMessage: '',
    likedCommentsCount: 0
  }),
  computed: {
    ...mapGetters(['articlesAnalysis', 'commentsAnalysis'])
  },
  methods: {
    ...mapActions([FETCH_ARTICLES_ANALYSIS, FETCH_COMMENTS_ANALYSIS])
  },
  created() {
    this.fetchCommentsAnalysis();
  },
  async mounted() {
    this.loaded = false;
    try {
      await this.fetchArticlesAnalysis();

      const mostEmotionId = this.articlesAnalysis.mostEmotionId;
      this.articlesAnalysisMessage = ARTICLES_ANALYSIS_MESSAGES.get(
        mostEmotionId
      )
        .split('\n')
        .join('<br />');

      this.chartdata = {
        labels: ['😃', '😢', '😠', '😶', '😲', '🤒'],
        datasets: [
          {
            label: '작성한 일기의 개수',
            borderColor: '#B2A4D4',
            data: this.articlesAnalysis.articleEmotionsCount
          }
        ]
      };

      this.options = {
        scale: {
          pointLabels: {
            fontSize: 30
          }
        }
      };

      this.loaded = true;
    } catch (e) {
      console.error(e);
    }
  }
};
</script>
