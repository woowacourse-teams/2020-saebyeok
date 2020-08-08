<template>
  <v-container>
    <v-layout>
      <detail-page-card :article="article" />
    </v-layout>

    <v-layout>
      <v-row v-if="article.isCommentAllowed" dense>
        <v-col v-for="comment in article.comments" :key="comment.id" cols="12">
          <comment :comment="comment"></comment>
        </v-col>
      </v-row>
      <v-row v-else>
        <v-col
          class="d-flex justify-center"
          style="font-size: 15px; font-color: #FFFFFF; line-height: 15px;"
        >
          {{ commentNotAllowedMessage }}
        </v-col>
      </v-row>
    </v-layout>

    <v-layout v-if="article.isCommentAllowed">
      <comment-create-form :articleId="article.id" />
    </v-layout>
  </v-container>
</template>

<script>
import { mapActions, mapGetters } from 'vuex';
import { FETCH_ARTICLE } from '@/store/shared/actionTypes';
import DetailPageCard from '@/components/DetailPageCard';
import Comment from '@/components/Comment';
import CommentCreateForm from '@/components/CommentCreateForm';

export default {
  name: 'ArticleDetail',
  data() {
    return {
      commentNotAllowedMessage: '댓글을 작성할 수 없는 글입니다.'
    };
  },
  components: {
    DetailPageCard,
    Comment,
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
