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

    <v-layout column v-if="article.isCommentAllowed" max-width="400">
      <v-flex>
        <v-textarea
          solo
          no-resize
          counter="140"
          maxlength="140"
          rows="3"
          name="input-7-4"
          label="댓글을 달아주세요~"
          v-model="content"
        ></v-textarea>
      </v-flex>
      <v-flex>
        <v-btn
          class="ma-2"
          depressed
          block
          dark
          width="100%"
          color="rgba(230, 197, 234)"
          @click="submitComment"
          >남기기</v-btn
        >
      </v-flex>
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
  data() {
    return {
      commentNotAllowedMessage: '댓글을 작성할 수 없는 글입니다.'
    };
  },
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
    ...mapActions([FETCH_ARTICLE]),
    submitComment() {
      return;
    }
  }
};
</script>
