<template>
  <v-card color="#faf9f5" max-width="400">
    <v-card-title>
      {{ comment.nickname }}
    </v-card-title>

    <v-card-text class="text-h6">
      <div v-if="comment.isDeleted">
        {{ deletedCommentMessage }}
      </div>
      <div v-else>
        {{ comment.content }}
      </div>
    </v-card-text>

    <v-card-actions>
      <v-list-item class="grow">
        <v-row>
          <v-col align="left" cols="10" justify="end">
            <div style="float:left;">
              <v-icon class="mr-1">mdi-hand-heart</v-icon>
              <span class="subheading mr-2">{{ recommend }}</span>
            </div>
          </v-col>
          <v-col align="right" cols="2" justify="end">
            <v-icon class="mr-1">mdi-alarm-light</v-icon>
          </v-col>
        </v-row>
      </v-list-item>
    </v-card-actions>
  </v-card>
</template>

<script>
export default {
  //id, content, nickname, isDeleted
  name: 'Comment',
  data() {
    return {
      //아직 article에 없는 값을 임시로 설정
      emotion: '😊',
      tags: ['# 즐거워요', '# 기뻐요', '# 행복해요'],
      recommend: 42,
      deletedCommentMessage: '삭제된 댓글입니다.'
    };
  },
  computed: {
    createdDate: function() {
      const date = new Date(this.comment.createdDate);
      const now = new Date();
      const gap = (now.getTime() - date.getTime()) / 1000;
      if (gap > 86400) {
        return (
          date.getFullYear() +
          '년 ' +
          (date.getMonth() + 1) +
          '월 ' +
          date.getDate() +
          '일'
        );
      } else if (gap > 3600) {
        return Math.floor(gap / 3600) + '시간 전';
      } else if (gap > 60) {
        return Math.floor(gap / 60) + '분 전';
      } else {
        return '방금 전';
      }
    }
  },
  props: {
    comment: {
      type: Object,
      required: true
    }
  }
};
</script>
