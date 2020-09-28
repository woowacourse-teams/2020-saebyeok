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
          maxlength="140"
        />
        <div class="align-top">
          <v-icon class="ma-2" @click="submitComment" :color="buttonColor">
            mdi-send
          </v-icon>
        </div>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
import { mapActions, mapMutations } from 'vuex';
import { SHOW_SNACKBAR } from '@/store/shared/mutationTypes';
import { CREATE_COMMENT } from '@/store/shared/actionTypes';
import { STATUS } from '../../utils/Status';

export default {
  name: 'CommentCreateForm',
  data() {
    return {
      content: '',
      buttonColor: '#666666'
    };
  },
  methods: {
    ...mapActions([CREATE_COMMENT]),
    ...mapMutations([SHOW_SNACKBAR]),
    async submitComment() {
      this.buttonColor = '#96589b';

      this.content = this.content.trim();
      if (this.content.length === 0) {
        this.showSnackbar('댓글은 최소 1글자 이상 작성해주셔야 해요');
        this.buttonColor = '#666666';
        return;
      }

      const commentCreateRequest = {
        content: this.content,
        articleId: this.articleId
      };

      this.createComment(commentCreateRequest).then(response => {
        if (response.status === STATUS.CREATED) {
          this.$router.go();
        }
      });
    }
  },
  props: ['articleId']
};
</script>
