<template>
  <v-container ma-0 pa-0>
    <v-card class="mx-auto" max-width="400">
      <v-card-title class="pa-2">
        <v-layout align-center="">
          <emotion-image :emotion="article.emotion" />
          <sub-emotion-chips :subEmotions="article.subEmotions" />
          <v-flex justify-end>
            <detail-card-menu v-if="article.isMine && !this.isDetailPage()" />
          </v-flex>
        </v-layout>
      </v-card-title>

      <v-card-text class="headline text-body-1 pb-0" style="color:rgb(0,0,0)">
        {{ article.content }}
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
import DetailCardMenu from '@/components/card/DetailCardMenu.vue';
import { mapActions } from 'vuex';
import { LIKE_ARTICLE, UNLIKE_ARTICLE } from '@/store/shared/actionTypes';

export default {
  name: 'Card',
  components: {
    CreatedDate,
    EmotionImage,
    SubEmotionChips,
    DetailCardMenu
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
    isDetailPage() {
      return (
        this.$route.params.articleId === undefined ||
        this.$route.params.articleId === null
      );
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
