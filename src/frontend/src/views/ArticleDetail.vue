<template>
  <div>
    <card :article="article" />
  </div>
</template>

<script>
import { mapActions, mapGetters } from 'vuex';
import { FETCH_ARTICLE } from '@/store/shared/actionTypes';
import Card from '@/components/Card';

export default {
  name: 'ArticleDetail',
  components: {
    Card
  },
  created() {
    this.fetchArticle(this.$route.params.articleId);
  },
  computed: {
    ...mapGetters(['article'])
  },
  methods: {
    ...mapActions([FETCH_ARTICLE])
  }
};
</script>
