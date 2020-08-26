<template>
  <v-container pa-0 class="d-flex justify-center" style="max-width: 400px">
    <v-layout column>
      <v-flex class="d-flex justify-end align-center">
        <v-textarea
          solo
          auto-grow
          hide-details
          rows="1"
          name="input-7-4"
          label="댓글을 달아주세요~ (최대 140자)"
          v-model="content"
        />
        <div class="align-top">
          <v-icon class="ma-2" @click="submitComment">
            mdi-send
          </v-icon>
        </div>
      </v-flex>
    </v-layout>
  </v-container>
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
