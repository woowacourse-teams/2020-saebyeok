<template>
  <v-container pa-0>
    <v-layout mb-5>
      <card :article="article" />
    </v-layout>

    <comments />

    <v-footer
      app
      shift
      color="rgb(245,245,245)"
      class="pt-0"
      v-if="article.isCommentAllowed"
      :class="{ navbarSpace: navbarSpaceNeeded }"
    >
      <comment-create-form :articleId="article.id" />
    </v-footer>
  </v-container>
</template>

<script>
import { mapActions, mapGetters, mapMutations } from 'vuex';
import {
  CHECK_NAVBAR_SPACE_NEEDED,
  FETCH_ARTICLE
} from '@/store/shared/actionTypes';
import { DEACTIVATE_RECOMMENT } from '@/store/shared/mutationTypes';
import Card from '@/components/card/Card';
import Comments from '@/components/comment/Comments';
import CommentCreateForm from '@/components/comment/CommentCreateForm';

export default {
  name: 'ArticleDetail',
  components: {
    Card,
    Comments,
    CommentCreateForm
  },
  created() {
    this.fetchArticle(this.$route.params.articleId);
    this.checkNavbarSpaceNeeded();
    this.deactivateRecomment();
  },
  computed: {
    ...mapGetters(['article']),
    navbarSpaceNeeded() {
      return this.$store.getters.spaceNeeded;
    }
  },
  methods: {
    ...mapMutations([DEACTIVATE_RECOMMENT]),
    ...mapActions([FETCH_ARTICLE, CHECK_NAVBAR_SPACE_NEEDED])
  }
};
</script>

<style scoped>
.navbarSpace {
  bottom: 66px !important;
}
</style>
