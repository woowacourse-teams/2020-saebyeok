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

    <v-card-text class="pt-0 pl-1 pb-0 overflow-hidden">
      <div v-if="comment.isDeleted">
        {{ deletedCommentMessage }}
      </div>
      <div v-else>
        {{ comment.content }}
      </div>
    </v-card-text>

    <v-card-actions class="pr-3 pl-0">
      <v-list-item class="grow pa-0">
        <v-layout pa-1 v-if="!comment.isDeleted">
          <div style="float:left;">
            <div class="like-button" v-on:click="toggleLike">
              <v-icon
                v-if="comment.isLikedByMe"
                style="color: #96589b;"
                class="mr-1"
                >mdi-hand-heart
              </v-icon>
              <v-icon v-else class="mr-1">mdi-hand-heart-outline </v-icon>
              <span class="subheading mr-2">{{ comment.likesCount }}</span>
            </div>
          </div>
        </v-layout>
      </v-list-item>
    </v-card-actions>
  </v-card>
</template>

<script>
import CreatedDate from '@/components/CreatedDate';
import CommentMenu from '@/components/comment/CommentMenu';
import { mapActions } from 'vuex';
import { LIKE_COMMENT, UNLIKE_COMMENT } from '@/store/shared/actionTypes';

export default {
  name: 'Comment',
  data() {
    return {
      deletedCommentMessage: '삭제된 댓글입니다.'
    };
  },
  components: {
    CreatedDate,
    CommentMenu
  },
  props: {
    comment: {
      type: Object,
      required: true
    }
  },
  methods: {
    ...mapActions([LIKE_COMMENT, UNLIKE_COMMENT]),
    toggleLike() {
      if (this.comment.isLikedByMe) {
        this.unlikeComment(this.comment.id).then(() => {
          this.comment.isLikedByMe = !this.comment.isLikedByMe;
          this.comment.likesCount--;
        });
      } else {
        this.likeComment(this.comment.id).then(() => {
          this.comment.isLikedByMe = !this.comment.isLikedByMe;
          this.comment.likesCount++;
        });
      }
    }
  }
};
</script>
