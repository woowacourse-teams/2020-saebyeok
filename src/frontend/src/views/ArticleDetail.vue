<template>
  <div>
    <detail-page-card :article="article" />
  </div>
</template>

<script>
import { mapActions, mapGetters } from 'vuex';
import { FETCH_ARTICLE } from '@/store/shared/actionTypes';
import DetailPageCard from '@/components/DetailPageCard';

export default {
  name: 'ArticleDetail',
  components: {
    DetailPageCard
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
