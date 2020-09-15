<template>
  <v-container pa-0>
    <v-layout mb-5>
      <card :article="memberArticle" />
    </v-layout>
    <comments :article="memberArticle" />
    <v-footer
      app
      shift
      color="rgb(245,245,245)"
      pa-0
      ma-0
      v-if="memberArticle.isCommentAllowed"
      :class="{ navbarSpace: navbarSpaceNeeded }"
    >
      <comment-create-form :articleId="memberArticle.id" />
    </v-footer>
  </v-container>
</template>

<script>
import { mapActions, mapGetters } from 'vuex';
import {
  FETCH_MEMBER_ARTICLE,
  CHECK_NAVBAR_SPACE_NEEDED
} from '@/store/shared/actionTypes';
import Card from '@/components/card/Card';
import Comments from '@/components/comment/Comments';
import CommentCreateForm from '@/components/comment/CommentCreateForm';

export default {
  name: 'DiaryDetail',
  components: {
    Card,
    Comments,
    CommentCreateForm
  },
  created() {
    this.fetchMemberArticle(this.$route.params.articleId);
    this.checkNavbarSpaceNeeded();
  },
  computed: {
    ...mapGetters(['memberArticle']),
    navbarSpaceNeeded() {
      return this.$store.getters.spaceNeeded;
    }
  },
  methods: {
    ...mapActions([FETCH_MEMBER_ARTICLE, CHECK_NAVBAR_SPACE_NEEDED])
  }
};
</script>

<style scoped>
.navbarSpace {
  bottom: 66px !important;
}
</style>
