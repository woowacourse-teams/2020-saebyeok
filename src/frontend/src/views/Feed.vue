<template>
  <div>
    <h1>피드 화면</h1>
    <card></card>
  </div>
</template>

<script>
import Card from '@/components/Card.vue';
import ApiService from '@/api/index.js';

export default {
  data() {
    return { articles: {} };
  },
  components: {
    Card
  },
  created() {
    this.articles = ApiService.get('/api/articles');
  },
  props: {
    source: String
  }
};
</script>
