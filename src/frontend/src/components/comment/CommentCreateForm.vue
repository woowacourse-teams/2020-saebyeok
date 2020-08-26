<template>
  <v-container pa-0 class="d-flex justify-center" style="max-width: 400px">
    <v-layout column>
      <v-flex class="d-flex justify-center">
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
      <v-flex class="d-flex justify-center">
        <v-btn
          class="ma-2"
          depressed
          block
          dark
          width="100%"
          color="rgba(164, 63, 176)"
          @click="submitComment"
          >남기기</v-btn
        >
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
import { mapActions, mapMutations } from 'vuex';
import { SHOW_SNACKBAR } from '@/store/shared/mutationTypes';
import { CREATE_COMMENT } from '@/store/shared/actionTypes';
import { STATUS } from '../utils/Status';

export default {
  name: 'CommentCreateForm',
  data() {
    return {
      content: ''
    };
  },
  methods: {
    ...mapActions([CREATE_COMMENT]),
    ...mapMutations([SHOW_SNACKBAR]),
    async submitComment() {
      const commentCreateRequest = {
        content: this.content,
        articleId: this.articleId,
        isDeleted: false
      };
      this.createComment(commentCreateRequest)
        .then(response => {
          if (response.status === STATUS.CREATED) {
            this.$router.go();
          }
        })
        .catch(error => {
          this.showSnackbar(error.response.data.errorMessage);
        });
    }
  },
  props: ['articleId']
};
</script>
