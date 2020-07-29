<template>
  <v-container ma-0 pa-0>
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

    <v-layout>
      여기에 입력칸이 들어갑니다.
    </v-layout>
  </v-container>
</template>

<script>
import { mapActions, mapGetters } from 'vuex';
import { FETCH_ARTICLE } from '@/store/shared/actionTypes';
import DetailPageCard from '@/components/DetailPageCard';
import Comment from '@/components/Comment';

export default {
  name: 'ArticleDetail',
  components: {
    DetailPageCard,
    Comment
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
