<template>
  <div>
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
    this.articles = ApiService.get('/articles');
  },
  props: {
    source: String
  }
};
</script>
