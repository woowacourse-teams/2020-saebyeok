<template>
  <div>
    <hr noshade color="#ddd" />
    <v-card flat max-width="400" color="rgb(245,245,245)">
      <v-card-title class="pa-1 pb-0">
        <div class="ml-2" style="font-size:14px; color:black;">
          {{ recomment.nickname }}
        </div>
        <v-spacer />
        <v-card-actions class="pa-0">
          <v-layout v-if="!recomment.isDeleted">
            <div style="float:left;" class="mr-2">
              <div class="like-button" v-on:click="toggleLike">
                <v-icon
                  v-if="recomment.isLikedByMe"
                  style="font-size:20px; color: #96589b;"
                  class="mr-1"
                  >mdi-hand-heart</v-icon
                >
                <v-icon v-else style="font-size:20px;" class="mr-1"
                  >mdi-hand-heart-outline</v-icon
                >
                <span style="font-size:16px;">{{ recomment.likesCount }}</span>
              </div>
            </div>
          </v-layout>
        </v-card-actions>
        <comment-menu
          v-if="recomment.isMine && !recomment.isDeleted"
          :commentId="recomment.id"
        />
      </v-card-title>

      <v-card-text class="headline text-body-1 pb-0" style="color:rgb(0,0,0)">
        <div v-if="recomment.isDeleted" style="font-size:16px; color:black;">
          {{ deletedCommentMessage }}
        </div>
        <div v-else style="font-size:16px; color:black;">
          {{ recomment.content }}
        </div>
      </v-card-text>
      <div class="pl-4 pb-2">
        <created-date :createdDate="recomment.createdDate" />
      </div>
    </v-card>
  </div>
</template>

<script>
import CreatedDate from '@/components/CreatedDate';
import CommentMenu from '@/components/comment/CommentMenu';
import { mapActions } from 'vuex';
import { LIKE_COMMENT, UNLIKE_COMMENT } from '@/store/shared/actionTypes';

export default {
  name: 'Recomment',
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
    recomment: {
      type: Object,
      required: true
    }
  },
  methods: {
    ...mapActions([LIKE_COMMENT, UNLIKE_COMMENT]),
    toggleLike() {
      if (this.recomment.isLikedByMe) {
        this.unlikeComment(this.recomment.id).then(() => {
          this.recomment.isLikedByMe = false;
          this.recomment.likesCount--;
        });
      } else {
        this.likeComment(this.recomment.id).then(() => {
          this.recomment.isLikedByMe = true;
          this.recomment.likesCount++;
        });
      }
    }
  }
};
</script>
