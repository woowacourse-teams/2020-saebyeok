<template>
  <v-container ma-0 pa-0>
    <v-card class="mx-auto rounded-lg" color="#faf9f5" max-width="400">
      <v-card-title>
        <v-row no-gutters>
          <v-col cols="3">
            <emotion-tab :emotion="emotion" />
          </v-col>
          <v-col cols="9">
            <v-row no-gutters>
              <v-col
                cols="12"
                class="d-flex justify-end align-center"
                style="font-size: 15px; line-height: 15px"
              >
                <div>
                  <created-date :createdDate="article.createdDate" />
                </div>
                <div>
                  <v-btn icon>
                    <v-icon>mdi-dots-vertical</v-icon>
                  </v-btn>
                </div>
              </v-col>
            </v-row>
            <v-row no-gutters>
              <v-col
                cols="12"
                class="d-flex justify-end"
                style="font-size: 15px; line-height: 15px"
              >
                <sub-emotion-tab :subEmotions="subEmotions" />
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
                <v-icon class="mr-1">mdi-hand-heart</v-icon>
                <span class="subheading mr-2">{{ recommend }}</span>
              </div>
              <div v-if="article.isCommentAllowed" style="float:left;">
                <v-icon class="mr-1">mdi-comment</v-icon>
                <span class="subheading">{{ article.comments.length }}</span>
              </div>
            </v-col>
            <v-col align="right" cols="2" justify="end">
              <v-icon class="mr-1">mdi-alarm-light</v-icon>
            </v-col>
          </v-row>
        </v-list-item>
      </v-card-actions>
    </v-card>
  </v-container>
</template>

<script>
import CreatedDate from '@/components/CreatedDate';
import EmotionTab from '@/components/Card/EmotionTab';
import SubEmotionTab from '@/components/Card/SubEmotionTab';
export default {
  name: 'DetailPageCard',
  components: {
    CreatedDate,
    EmotionTab,
    SubEmotionTab
  },
  data() {
    return {
      //아직 article에 없는 값을 임시로 설정
      emotion: {
        id: 1,
        name: '화나요',
        imageResource:
          'https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/240/emojipedia/240/smiling-face-with-tear_1f972.png'
      },
      subEmotions: [
        { id: 1, name: '서브1' },
        { id: 2, name: '서브2' },
        { id: 3, name: '서브3' }
      ],
      tags: ['# 즐거워요', '# 기뻐요', '# 행복해요'],
      recommend: 42
    };
  },
  props: {
    article: {
      type: Object,
      required: true
    }
  }
};
</script>
