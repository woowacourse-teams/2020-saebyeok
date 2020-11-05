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
        style="color:rgb(0,0,0); min-height:10px; white-space: pre-wrap;"
        v-text="article.content"
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
                v-if="article.isCommentAllowed && article.commentsSize > 0"
                style="float:left;"
              >
                <v-icon style="font-size:20px;" class="mr-1"
                  >mdi-comment-outline</v-icon
                >
                <span class="subheading">{{ article.commentsSize }}</span>
              </div>
              <v-spacer />
            </v-col>
            <v-col align="right" justify="end" style="padding:0px" cols="2">
              <report-button
                v-if="!article.isMine"
                @click="changeReportTarget()"
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

import { mapActions, mapMutations } from 'vuex';
import { LIKE_ARTICLE, UNLIKE_ARTICLE } from '@/store/shared/actionTypes';
import { SET_REPORT_TARGET } from '@/store/shared/mutationTypes';
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
    ...mapMutations([SET_REPORT_TARGET]),
    ...mapActions([LIKE_ARTICLE, UNLIKE_ARTICLE]),
    toggleLike() {
      event.stopPropagation();
      if (this.article.isLikedByMe) {
        this.unlikeArticle(this.article);
      } else {
        this.likeArticle(this.article);
      }
    },
    changeReportTarget() {
      this.setReportTarget({
        target: REPORT_TARGET.ARTICLE,
        contentId: this.article.id
      });
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
