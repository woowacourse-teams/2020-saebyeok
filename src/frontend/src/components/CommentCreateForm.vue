<template>
  <v-layout column max-width="400">
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
</template>

<script>
import { mapActions } from 'vuex';
import { CREATE_COMMENT } from '@/store/shared/actionTypes';
export default {
  name: 'CommentCreateForm',
  data() {
    return {
      content: ''
    };
  },
  methods: {
    ...mapActions([CREATE_COMMENT]),
    async submitComment() {
      const commentCreateRequest = {
        content: this.content,
        memberId: 1,
        nickname: 'TEST_NICKNAME',
        articleId: this.articleId,
        isDeleted: false
      };
      this.createComment(commentCreateRequest).then(response => {
        if (response.status === 201) {
          console.log(response.status);
          this.$router.go();
        }
      });
    }
  },
  props: ['articleId']
};
</script>
