<template>
  <v-container>
    <v-layout>
      <detail-page-card :article="memberArticle" />
    </v-layout>
    <comments :article="memberArticle" />
    <v-layout v-if="memberArticle.isCommentAllowed">
      <comment-create-form :articleId="memberArticle.id" />
    </v-layout>
  </v-container>
</template>

<script>
import { mapActions, mapGetters } from 'vuex';
import { FETCH_MEMBER_ARTICLE } from '@/store/shared/actionTypes';
import DetailPageCard from '@/components/card/DetailPageCard';
import Comments from '@/components/comment/Comments';
import CommentCreateForm from '@/components/comment/CommentCreateForm';

export default {
  name: 'DiaryDetail',
  components: {
    DetailPageCard,
    Comments,
    CommentCreateForm
  },
  created() {
    this.fetchMemberArticle(this.$route.params.articleId);
  },
  computed: {
    ...mapGetters(['memberArticle'])
  },
  methods: {
    ...mapActions([FETCH_MEMBER_ARTICLE])
  }
};
</script>
