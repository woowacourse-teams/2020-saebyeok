<template>
  <div>
    <v-container>
      <v-layout column>
        <v-row no-gutters>
          <v-col cols="2" style="line-height: 60px">
            <v-img
              :src="emotion.imageResource"
              :alt="emotion.name"
              max-height="60"
              max-width="60"
            >
            </v-img>
          </v-col>
          <v-col cols="10">
            <v-chip-group
              column
              multiple
              max="3"
              active-class="black--text text--accent-4"
              align="right"
              justify="end"
            >
              <v-chip
                v-for="subEmotion in emotion.subEmotions"
                :key="subEmotion.id"
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
    import {CREATE_ARTICLE, FETCH_EMOTION} from '@/store/shared/actionTypes';
    import {mapActions, mapGetters} from 'vuex';

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
    async submit() {
      const articleCreateRequest = {
        //todo : 여기서 emotion : this.emotion.id,
        //subEmotions : this.chooseSubEmotion 을 전달하면 된다.
        content: this.content,
        emotion: this.emotion.name + this.chooseSubEmotions[0],
        isCommentAllowed: this.isCommentAllowed
      };
      this.createArticle(articleCreateRequest).then(response => {
        if (response.status === 201) {
          this.$router.replace({ name: 'Feed' });
        }
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
