<template>
  <div>
    <hr noshade color="#ddd" />
    <v-card flat max-width="400" color="rgb(245,245,245)">
      <v-card-title class="pa-1 pb-0">
        <div class="ml-2" style="font-size:14px; color:black;">
          {{ comment.nickname }}
        </div>
        <v-spacer />
        <v-card-actions class="pa-0">
          <v-layout v-if="!comment.isDeleted">
            <div style="float:left;" class="mr-2">
              <div class="like-button" v-on:click="toggleLike">
                <v-icon
                  v-if="comment.isLikedByMe"
                  style="font-size:20px; color: #96589b;"
                  class="mr-1"
                  >mdi-hand-heart</v-icon
                >
                <v-icon v-else style="font-size:20px;" class="mr-1"
                  >mdi-hand-heart-outline</v-icon
                >
                <span style="font-size:16px;">{{ comment.likesCount }}</span>
              </div>
            </div>
          </v-layout>
        </v-card-actions>
        <comment-menu
          v-if="comment.isMine && !comment.isDeleted"
          :commentId="comment.id"
        />
      </v-card-title>

      <v-card-text class="headline text-body-1 pb-0" style="color:rgb(0,0,0)">
        <div v-if="comment.isDeleted" style="font-size:16px; color:black;">
          {{ deletedCommentMessage }}
        </div>
        <div v-else style="font-size:16px; color:black;">
          {{ comment.content }}
        </div>
      </v-card-text>
      <div class="pl-4 pb-2">
        <created-date :createdDate="comment.createdDate" />
      </div>
    </v-card>
    <!-- 추후에 아래의 recomments를 comment.recomments로 수정해야 함 -->
    <recomments :recomments="recomments" />
  </div>
</template>

<script>
import CreatedDate from '@/components/CreatedDate';
import CommentMenu from '@/components/comment/CommentMenu';
import Recomments from '@/components/comment/Recomments';
import { mapActions } from 'vuex';
import { LIKE_COMMENT, UNLIKE_COMMENT } from '@/store/shared/actionTypes';

export default {
  name: 'Comment',
  data() {
    return {
      deletedCommentMessage: '삭제된 댓글입니다.',
      recomments: [
        {
          id: 100,
          content: '대댓글 내용',
          nickname: '대댓글닉네임',
          isDeleted: false,
          createdDate: '2020-09-14T20:57:22',
          isMine: true,
          likesCount: 2,
          isLikedByMe: false
        },
        {
          id: 101,
          content: '대댓글 내용2',
          nickname: '대댓글닉네임2',
          isDeleted: false,
          createdDate: '2020-09-14T20:58:22',
          isMine: false,
          likesCount: 3,
          isLikedByMe: false
        },
        {
          id: 102,
          content: '대댓글 내용22222222222222222222222222222222222222222222222',
          nickname: '대댓글닉네임2',
          isDeleted: false,
          createdDate: '2020-09-14T20:59:22',
          isMine: false,
          likesCount: 3,
          isLikedByMe: false
        }
      ]
    };
  },
  components: {
    CreatedDate,
    CommentMenu,
    Recomments
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
          this.comment.isLikedByMe = false;
          this.comment.likesCount--;
        });
      } else {
        this.likeComment(this.comment.id).then(() => {
          this.comment.isLikedByMe = true;
          this.comment.likesCount++;
        });
      }
    }
  }
};
</script>
