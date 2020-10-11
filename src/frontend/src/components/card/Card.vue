<template>
  <v-container ma-0 pa-0>
    <v-card class="mx-auto" max-width="400">
      <v-card-title class="pa-2">
        <v-layout align-center="">
          <emotion-image :emotion="article.emotion" />
          <sub-emotion-chips :subEmotions="article.subEmotions" />
          <v-flex justify-end>
            <detail-card-menu
              :articleId="this.article.id"
              v-if="article.isMine"
            />
          </v-flex>
        </v-layout>
      </v-card-title>
      <v-card-text
        class="headline text-body-1 pb-0"
        style="color:rgb(0,0,0)"
        v-html="article.content.replace(/(?:\r\n|\r|\n)/g, '<br />')"
        v-linkified
        @click="clickCardContent()"
      >
      </v-card-text>

      <v-card-actions>
        <v-list-item class="grow">
          <v-row class="pl-1">
            <v-col
              cols="12"
              class="d-flex align-center"
              style="font-size: 15px; line-height: 15px; padding:0px"
              justify-start
            >
              <div>
                <created-date :createdDate="article.createdDate" />
              </div>
            </v-col>
            <v-col align="left" cols="10" justify="end" style="padding:0px">
              <div style="float:left;">
                <div class="like-button" v-on:click="toggleLike">
                  <v-icon
                    v-if="article.isLikedByMe"
                    style="font-size:20px; color: #96589b;"
                    class="mr-1"
                    >mdi-hand-heart
                  </v-icon>
                  <v-icon v-else style="font-size:20px;" class="mr-1"
                    >mdi-hand-heart-outline
                  </v-icon>
                  <span class="subheading mr-2">{{ article.likesCount }}</span>
                </div>
              </div>
              <div
                v-if="article.isCommentAllowed && article.comments.length > 0"
                style="float:left;"
              >
                <v-icon style="font-size:20px;" class="mr-1"
                  >mdi-comment-outline</v-icon
                >
                <span class="subheading">{{ article.comments.length }}</span>
              </div>
              <v-spacer />
            </v-col>
            <v-col align="right" justify="end" style="padding:0px" cols="2">
              <report-button
                v-if="!article.isMine"
                :reportTarget="getReportTarget()"
                :reportedId="article.id"
              />
            </v-col>
          </v-row>
        </v-list-item>
      </v-card-actions>
    </v-card>
  </v-container>
</template>

<script>
import CreatedDate from '@/components/CreatedDate';
import EmotionImage from '@/components/card/EmotionImage';
import SubEmotionChips from '@/components/card/SubEmotionChips';
import ReportButton from '@/components/ReportButton';
import DetailCardMenu from '@/components/card/DetailCardMenu';
import { REPORT_TARGET } from '@/utils/ReportTarget.js';

import { mapActions } from 'vuex';
import { LIKE_ARTICLE, UNLIKE_ARTICLE } from '@/store/shared/actionTypes';
import linkify from 'vue-linkify';

export default {
  name: 'Card',
  components: {
    CreatedDate,
    EmotionImage,
    SubEmotionChips,
    ReportButton,
    DetailCardMenu
  },
  directives: {
    linkified: linkify
  },
  methods: {
    ...mapActions([LIKE_ARTICLE, UNLIKE_ARTICLE]),
    toggleLike() {
      event.stopPropagation();
      if (this.article.isLikedByMe) {
        this.unlikeArticle(this.article.id).then(() => {
          this.article.isLikedByMe = false;
          this.article.likesCount--;
        });
      } else {
        this.likeArticle(this.article.id).then(() => {
          this.article.isLikedByMe = true;
          this.article.likesCount++;
        });
      }
    },
    getReportTarget() {
      return REPORT_TARGET.ARTICLE;
    },
    clickCardContent() {
      this.$emit('clickCardContent');
    }
  },
  props: {
    article: {
      type: Object,
      required: true
    }
  }
};
</script>
