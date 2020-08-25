<template>
  <v-container ma-0 pa-0>
    <v-card
      class="mx-auto rounded-lg"
      color="#faf9f5"
      max-width="400"
      v-on:click="onClickCard"
    >
      <v-card-title>
        <v-row no-gutters>
          <v-col cols="3">
            <emotion-image :emotion="article.emotion" />
          </v-col>
          <v-col cols="9">
            <v-row no-gutters>
              <v-col
                cols="12"
                class="d-flex justify-end align-center"
                style="font-size: 15px; line-height: 15px"
              >
                <created-date :createdDate="article.createdDate" />
              </v-col>
            </v-row>
            <v-row no-gutters>
              <v-col
                cols="12"
                class="d-flex justify-end"
                style="font-size: 15px; line-height: 15px"
              >
                <sub-emotion-chips :subEmotions="article.subEmotions" />
              </v-col>
            </v-row>
          </v-col>
        </v-row>
      </v-card-title>

      <v-card-text class="headline text-body-1">
        {{ article.content }}
      </v-card-text>

      <v-card-actions>
        <v-list-item class="grow">
          <v-row>
            <v-col align="left" cols="10" justify="end">
              <div style="float:left;">
                <div class="like-button" v-on:click="toggleLike">
                  <v-icon class="mr-1" :class="{ liked: likedByMe }"
                    >mdi-hand-heart
                  </v-icon>
                  <span class="subheading mr-2">{{ likesCount }}</span>
                </div>
              </div>
              <div v-if="article.isCommentAllowed" style="float:left;">
                <v-icon class="mr-1">mdi-comment</v-icon>
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
