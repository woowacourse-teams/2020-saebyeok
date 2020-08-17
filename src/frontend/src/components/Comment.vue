<template>
  <v-card class="mx-auto" color="#faf9f5" max-width="400">
    <v-card-title class="text-body-1">
      <v-row>
        <v-col cols="6">
          {{ comment.nickname }}
        </v-col>
        <v-col cols="6" justify-end>
          <created-date :createdDate="comment.createdDate" />
        </v-col>
      </v-row>
    </v-card-title>

    <v-card-text class="text-body-1">
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
              <div class="like-button" v-on:click="toggleLike">
                <v-icon class="mr-1" v-bind:class="{ liked: likedByMe }"
                  >mdi-hand-heart
                </v-icon>
                <span class="subheading mr-2">{{ likesCount }}</span>
              </div>
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
import CreatedDate from '@/components/CreatedDate';

export default {
  //id, content, nickname, isDeleted, createdDate
  name: 'Comment',
  components: {
    CreatedDate
  },
  data() {
    return {
      //아직 article에 없는 값을 임시로 설정
      emotion: '😊',
      tags: ['# 즐거워요', '# 기뻐요', '# 행복해요'],
      likesCount: 42, // 추후 백엔드에서 받아올 정보
      likedByMe: false, // 추후 백엔드에서 받아올 정보
      deletedCommentMessage: '삭제된 댓글입니다.'
    };
  },
  props: {
    comment: {
      type: Object,
      required: true
    }
  },
  methods: {
    toggleLike() {
      this.likedByMe = !this.likedByMe;
      this.likedByMe ? this.likesCount++ : this.likesCount--;
    }
  }
};
</script>

<style scoped>
.liked {
  color: #96589b;
}
</style>
