<template>
  <div>
    <v-card flat max-width="400" color="rgb(245,245,245)">
      <v-row class="flex-nowrap">
        <v-col cols="1" style="background-color: rgb(245,245,245);">
          <v-icon style="font-size:20px; color: #aaa;">
            mdi-subdirectory-arrow-right
          </v-icon>
        </v-col>
        <v-col cols="11" class="pa-0">
          <v-card-title class="pa-1 pb-0 pr-3">
            <div class="ml-2" style="font-size:14px; color:black;">
              {{ getCommentNickname() }}
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
                    <span style="font-size:16px;">{{
                      comment.likesCount
                    }}</span>
                  </div>
                </div>
              </v-layout>
            </v-card-actions>
            <comment-menu
              v-if="comment.isMine && !comment.isDeleted"
              :commentId="comment.id"
            />
          </v-card-title>

          <v-card-text
            class="headline text-body-1 pb-0"
            style="color:rgb(0,0,0)"
          >
            <div v-if="comment.isDeleted" style="font-size:16px; color:black;">
              {{ deletedCommentMessage }}
            </div>
            <div v-else style="font-size:16px; color:black;">
              {{ comment.content }}
            </div>
          </v-card-text>
          <v-flex row ma-0 pa-0 pr-3>
            <div class="pl-4 pb-2 pt-2">
              <created-date :createdDate="comment.createdDate" />
            </div>
            <v-spacer />
            <div class="pb-2 pr-2">
              <report-button
                v-if="!comment.isMine"
                :reportTarget="getReportTarget()"
                :targetContentId="comment.id"
              />
            </div>
          </v-flex>
        </v-col>
      </v-row>
    </v-card>
    <hr noshade color="#ddd" />
  </div>
</template>

<script>
import CreatedDate from '@/components/CreatedDate';
import CommentMenu from '@/components/comment/CommentMenu';
import ReportButton from '@/components/ReportButton';
import { REPORT_TARGET } from '@/utils/ReportTarget.js';
import { mapActions, mapGetters } from 'vuex';
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
    CommentMenu,
    ReportButton
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
      if (this.recomment.isLikedByMe) {
        this.unlikeComment(this.recomment);
      } else {
        this.likeComment(this.recomment);
      }
    },
    getReportTarget() {
      return REPORT_TARGET.COMMENT;
    },
    getCommentNickname() {
      return this.comment.nickname === '작성자'
        ? '✒️ ' + this.comment.nickname
        : this.comment.nickname;
    }
  },
  computed: {
    ...mapGetters(['article', 'comments'])
  }
};
</script>
