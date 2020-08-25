<template>
  <v-container>
    <v-layout>
      <detail-page-card :article="article" />
    </v-layout>

    <comments :article="article" />

    <v-footer
      app
      shift
      color="#e3d6f4"
      pa-0
      ma-0
      v-if="article.isCommentAllowed"
    >
      <comment-create-form :articleId="article.id" />
    </v-footer>
  </v-container>
</template>

<script>
import { mapActions, mapGetters } from 'vuex';
import { FETCH_ARTICLE } from '@/store/shared/actionTypes';
import DetailPageCard from '@/components/card/DetailPageCard';
import Comments from '@/components/comment/Comments';
import CommentCreateForm from '@/components/comment/CommentCreateForm';

export default {
  name: 'ArticleDetail',
  components: {
    DetailPageCard,
    Comments,
    CommentCreateForm
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
