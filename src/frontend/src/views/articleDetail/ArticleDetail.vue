<template>
  <v-container pa-0>
    <v-layout mb-5>
      <detail-page-card :article="article" />
    </v-layout>

    <comments :article="article" />

    <v-footer
      app
      shift
      color="rgb(245,245,245)"
      pa-0
      ma-0
      v-if="article.isCommentAllowed"
      :class="{ navbarSpace: navbarSpaceNeeded }"
    >
      <comment-create-form :articleId="article.id" />
    </v-footer>
  </v-container>
</template>

<script>
import { mapActions, mapGetters } from 'vuex';
import {
  FETCH_ARTICLE,
  CHECK_NAVBAR_SPACE_NEEDED
} from '@/store/shared/actionTypes';
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
    this.checkNavbarSpaceNeeded();
  },
  computed: {
    ...mapGetters(['article']),
    navbarSpaceNeeded() {
      return this.$store.getters.spaceNeeded;
    }
  },
  methods: {
    ...mapActions([FETCH_ARTICLE, CHECK_NAVBAR_SPACE_NEEDED])
  }
};
</script>

<style scoped>
.navbarSpace {
  bottom: 66px !important;
}
</style>
