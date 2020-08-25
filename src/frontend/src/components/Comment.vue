<template>
  <v-card class="mx-auto" color="#faf9f5" max-width="400">
    <v-card-title class="text-body-1">
      <v-row>
        <v-col cols="6">
          {{ comment.nickname }}
        </v-col>
        <v-col cols="6" justify-end>
          <created-date :createdDate="comment.createdDate" />
        </v-col>
      </v-row>
    </v-card-title>

    <v-card-text class="text-body-1">
      <div v-if="comment.isDeleted">
        {{ deletedCommentMessage }}
      </div>
      <div v-else>
        {{ comment.content }}
      </div>
    </v-card-text>

    <v-card-actions>
      <v-list-item class="grow">
        <v-row>
          <v-col align="left" cols="10" justify="end">
            <div style="float:left;">
              <div class="like-button" v-on:click="toggleLike">
                <v-icon class="mr-1" :class="{ liked: comment.isLikedByMe }"
                  >mdi-hand-heart
                </v-icon>
                <span class="subheading mr-2">{{ comment.likesCount }}</span>
              </div>
            </div>
          </v-col>
          <v-col align="right" cols="2" justify="end">
            <v-icon class="mr-1">mdi-alarm-light</v-icon>
          </v-col>
        </v-row>
      </v-list-item>
    </v-card-actions>
  </v-card>
</template>

<script>
import CreatedDate from '@/components/CreatedDate';
import { mapActions } from 'vuex';
import { LIKE_COMMENT, UNLIKE_COMMENT } from '@/store/shared/actionTypes';

export default {
  //id, content, nickname, isDeleted, createdDate
  name: 'Comment',
  components: {
    CreatedDate
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
        this.unlikeComment(this.comment.id);
        this.comment.isLikedByMe = !this.comment.isLikedByMe;
        this.comment.likesCount--;
      } else {
        this.likeComment(this.comment.id);
        this.comment.isLikedByMe = !this.comment.isLikedByMe;
        this.comment.likesCount++;
      }
    }
  }
};
</script>

<style scoped>
.liked {
  color: #96589b;
}
</style>
