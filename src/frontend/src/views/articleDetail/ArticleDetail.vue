<template>
  <v-container>
    <v-layout>
      <detail-page-card :article="article" />
    </v-layout>

    <comments :article="article" />

    <v-layout v-if="article.isCommentAllowed">
      <comment-create-form :articleId="article.id" />
    </v-layout>
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
