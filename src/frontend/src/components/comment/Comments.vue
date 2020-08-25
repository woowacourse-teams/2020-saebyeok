<template>
  <v-flex>
    <v-subheader class="pl-0">
      댓글 ({{ article.comments.length }})
    </v-subheader>
    <v-card class="mx-auto" color="#d1cbd1" max-width="400">
      <v-row v-if="article.isCommentAllowed" dense>
        <v-col
          v-for="comment in article.comments"
          :key="comment.id"
          cols="12"
          class="pa-0"
        >
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
    </v-card>
  </v-flex>
</template>
<script>
import Comment from '@/components/comment/Comment';
export default {
  name: 'Comments',
  components: {
    Comment
  },
  data() {
    return {
      commentNotAllowedMessage: '댓글을 작성할 수 없는 글입니다.'
    };
  },
  props: {
    article: {
      type: Object,
      required: true
    }
  }
};
</script>
