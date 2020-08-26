<template>
  <!-- <v-flex v-if="c.recomments.length > 0">
      <span class='depth' />
    </v-flex> 
    위의 세 줄은, 나중에 대댓글 쓸때 인덴트 넣어써 쓰면 됨
    -->
  <v-card style="padding:8px" flat max-width="400">
    <v-card-title class="pa-1">
      <div class="mr-3">
        {{ comment.nickname }}
      </div>
      <v-spacer />
      <div>
        <created-date :createdDate="comment.createdDate" />
      </div>
      <comment-menu
        v-if="comment.isMine && !comment.isDeleted"
        :commentId="comment.id"
      />
    </v-card-title>
    <v-card-text class="pt-0 pb-0 overflow-hidden">
      {{ comment.content }}
    </v-card-text>
    <v-card-actions class="pr-3">
      <div style="float:left;">
        <div class="like-button subheading" v-on:click="toggleLike">
          <v-icon class="mr-1" :class="{ liked: likedByMe }"
            >mdi-hand-heart
          </v-icon>
          <span class="subheading mr-2">{{ likesCount }}</span>
        </div>
      </div>
    </v-card-actions>
  </v-card>
</template>

<script>
import CreatedDate from '@/components/CreatedDate';
import CommentMenu from '@/components/comment/CommentMenu';

export default {
  name: 'Comment',
  components: {
    CreatedDate,
    CommentMenu
  },
  data() {
    return {
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
