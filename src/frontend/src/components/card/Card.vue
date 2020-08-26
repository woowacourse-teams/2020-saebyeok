<template>
  <v-container ma-0 pa-0>
    <v-card class="mx-auto" max-width="400" v-on:click="onClickCard">
      <v-card-title class="pb-2">
        <v-layout align-center="">
          <emotion-image :emotion="article.emotion" />
          <sub-emotion-chips :subEmotions="article.subEmotions" />
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
                    style="font-size:20px;"
                    class="mr-1"
                    :class="{ liked: likedByMe }"
                    >mdi-hand-heart-outline
                  </v-icon>
                  <span class="subheading mr-2">{{ likesCount }}</span>
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

export default {
  name: 'Card',
  components: {
    CreatedDate,
    EmotionImage,
    SubEmotionChips
  },
  data() {
    return {
      likesCount: 42, // 추후 백엔드에서 받아올 정보
      likedByMe: false // 추후 백엔드에서 받아올 정보
    };
  },
  methods: {
    onClickCard: function() {
      this.$router.push({
        path: this.$router.history.current.path + '/' + this.article.id
      });
    },
    toggleLike() {
      event.stopPropagation();
      this.likedByMe = !this.likedByMe;
      this.likedByMe ? this.likesCount++ : this.likesCount--;
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

<style scoped>
.liked {
  color: #96589b;
}
</style>
