<template>
  <div>
    <v-container>
      <v-layout column pa-0 ma-0>
        <v-row no-gutters>
          <v-col
            cols="3"
            style="line-height: 80px"
            align="center"
            justify="center"
          >
            <v-img
              :src="emotion.imageResource"
              :alt="emotion.name"
              max-height="80"
              max-width="80"
            >
            </v-img>
          </v-col>
          <v-col cols="9">
            <v-chip-group
              active-class="black--text text--accent-4"
              column
              multiple
              max="3"
              align="right"
              justify="space-between"
            >
              <v-chip
                v-for="subEmotion in emotion.subEmotions"
                :key="subEmotion.id"
                class="ma-1"
                style="font-size: 12px;"
                v-on:click="onClickSubEmotionTag(subEmotion.id)"
                ># {{ subEmotion.name }}</v-chip
              >
            </v-chip-group>
            <h5
              v-if="invalidEmotionLength"
              style="color: red; font-weight: lighter"
            >
              감정 태그는 3개까지 선택할 수 있어요.
            </h5>
          </v-col>
        </v-row>
        <br />
        <v-flex>
          <v-textarea
            solo
            no-resize
            counter="300"
            maxlength="300"
            rows="9"
            name="input-7-4"
            label="당신의 마음을 들려주세요."
            v-model="content"
          ></v-textarea>
        </v-flex>
        <v-flex>
          <v-checkbox
            v-model="isCommentAllowed"
            label="댓글을 허용할게요."
          ></v-checkbox>
        </v-flex>
        <v-flex>
          <v-btn
            class="ma-2"
            depressed
            block
            dark
            width="100%"
            color="rgba(164, 63, 176)"
            @click="submit"
            >남기기
          </v-btn>
        </v-flex>
      </v-layout>
    </v-container>
  </div>
</template>

<script>
import { CREATE_ARTICLE, FETCH_EMOTION } from '@/store/shared/actionTypes';
import { SHOW_SNACKBAR } from '@/store/shared/mutationTypes';
import { STATUS } from '../../utils/Status';

import { mapActions, mapGetters, mapMutations } from 'vuex';

export default {
  name: 'Post',
  created() {
    this.fetchEmotion(this.$route.params.emotionId);
  },
  data() {
    return {
      content: '',
      isCommentAllowed: true,
      chooseSubEmotions: [],
      invalidEmotionLength: false
    };
  },
  methods: {
    ...mapActions([CREATE_ARTICLE]),
    ...mapActions([FETCH_EMOTION]),
    ...mapMutations([SHOW_SNACKBAR]),
    async submit() {
      const articleCreateRequest = {
        content: this.content,
        emotionId: this.emotion.id,
        subEmotionIds: this.chooseSubEmotions,
        isCommentAllowed: this.isCommentAllowed
      };
      this.createArticle(articleCreateRequest)
        .then(response => {
          if (response.status === STATUS.CREATED) {
            this.$router.replace({ name: 'Feed' });
          }
        })
        .catch(error => {
          this.showSnackbar(error.response.data.errorMessage);
        });
    },
    onClickSubEmotionTag(subEmotionId) {
      if (this.chooseSubEmotions.includes(subEmotionId)) {
        const index = this.chooseSubEmotions.indexOf(subEmotionId);
        this.chooseSubEmotions.splice(index, 1);
        this.invalidEmotionLength = false;
        return;
      }
      if (this.chooseSubEmotions.length >= 3) {
        this.invalidEmotionLength = true;
        return;
      }
      this.chooseSubEmotions.push(subEmotionId);
      this.invalidEmotionLength = false;
    }
  },
  computed: {
    ...mapGetters(['emotion'])
  }
};
</script>
