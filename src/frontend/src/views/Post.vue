<template>
  <div>
    <v-col cols="12" md="8">
      <v-textarea
        solo
        no-resize
        name="input-7-4"
        label="당신의 마음을 들려주세요."
        v-model="content"
      ></v-textarea>
      <v-checkbox
        v-model="isCommentAllowed"
        label="댓글을 허용할게요."
      ></v-checkbox>
      <v-btn
        class="ma-2"
        outlined
        block
        width="100%"
        color="#df93bd"
        @click="submit"
        >남기기</v-btn
      >
    </v-col>
  </div>
</template>

<script>
import ApiService from '@/api/index.js';

export default {
  components: {},
  props: {
    source: String
  },
  data() {
    return {
      content: '',
      isCommentAllowed: true
    };
  },
  methods: {
    submit() {
      const articleCreateRequest = {
        content: this.content,
        emotion: '기뻐요',
        isCommentAllowed: this.isCommentAllowed
      };
      console.log(articleCreateRequest);
      ApiService.post('/articles', articleCreateRequest);
    }
  }
};
</script>
