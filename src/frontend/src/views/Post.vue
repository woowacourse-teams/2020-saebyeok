<template>
  <div>
    <v-container>
      <v-layout column>
        <v-row no-gutters>
          <v-col
            cols="2"
            style="font-size: 60px; text-align: center; line-height: 60px"
          >
            😊
          </v-col>
          <v-col cols="10">
            <v-chip-group
              column
              multiple
              active-class="black--text text--accent-4"
            >
              <v-chip v-for="tag in tags" :key="tag">
                {{ tag }}
              </v-chip>
            </v-chip-group>
          </v-col>
        </v-row>
        <br />
        <v-flex>
          <v-textarea
            solo
            no-resize
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
            color="rgba(230, 197, 234)"
            @click="submit"
            >남기기</v-btn
          >
        </v-flex>
      </v-layout>
    </v-container>
  </div>
</template>

<script>
  import ApiService from '@/api/index.js';

  export default {
  components: {},
  props: {
    source: String
  },
  data() {
    return {
      content: '',
      isCommentAllowed: true,
      tags: [
        '# 즐거워요',
        '# 기뻐요',
        '# 행복해요',
        '# 재밌어요',
        '# 만족스러워요',
        '# 흥미진진해요',
        '# 기대돼요'
      ]
    };
  },
  methods: {
    submit() {
      const articleCreateRequest = {
        content: this.content,
        emotion: '기뻐요',
        isCommentAllowed: this.isCommentAllowed
      };
      console.log(articleCreateRequest);
      ApiService.post('/articles', articleCreateRequest);
    }
  }
};
</script>
