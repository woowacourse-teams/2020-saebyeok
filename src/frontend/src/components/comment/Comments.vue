<template>
  <v-container ma-0 pa-0>
    <v-card
      v-if="article.isCommentAllowed"
      class="mx-auto"
      max-width="400"
      flat
      color="rgb(245,245,245)"
    >
      <v-row dense>
        <v-col
                v-for="comment in comments"
                :key="comment.id"
                cols="12"
                class="justify-center pt-0 pb-0"
        >
          <div v-if="comment.hasNoParent">
            <comment :comment="comment" />
          </div>
          <div v-else>
            <recomment :comment="comment" />
          </div>
        </v-col>
      </v-row>
    </v-card>
    <v-row v-else>
      <v-col
        class="d-flex justify-center"
        style="font-size: 15px; font-color: #FFFFFF; line-height: 15px;"
        >{{ commentNotAllowedMessage }}</v-col
      >
    </v-row>
  </v-container>
</template>
<script>
  import Comment from '@/components/comment/Comment';
  import Recomment from '@/components/comment/Recomment';

  import {mapActions, mapGetters} from 'vuex';
  import {FETCH_COMMENTS} from '@/store/shared/actionTypes';

  export default {
    name: 'Comments',
    components: {
      Comment,
      Recomment
    },
    created() {
      this.fetchComments(this.$route.params.articleId);
    },
    computed: {
      ...mapGetters(['comments']),
      ...mapGetters(['article'])
    },
    data() {
      return {
        commentNotAllowedMessage: '댓글을 작성할 수 없는 글입니다.'
      };
    },
    methods: {
      ...mapActions([FETCH_COMMENTS])
    }
  };
</script>
