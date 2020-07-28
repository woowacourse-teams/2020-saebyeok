<template>
  <v-card
    class="mx-auto rounded-lg"
    color="#faf9f5"
    max-width="400"
    v-on:click="onClickCard"
  >
    <v-card-title>
      <v-row no-gutters>
        <v-col
          class="d-flex justify-end"
          style="font-size: 15px; text-align: center; line-height: 15px"
        >
          {{ createdDate }}
        </v-col>
      </v-row>
      <v-row no-gutters>
        <v-col
          cols="2"
          style="font-size: 60px; text-align: center; line-height: 60px"
        >
          {{ emotion }}
        </v-col>
        <v-col cols="10">
          <v-chip-group
            active-class="black--text text--accent-4"
            column
            multiple
            align="right"
            justify="end"
          >
            <v-spacer />
            <v-chip :key="tag" v-for="tag in tags">
              {{ tag }}
            </v-chip>
          </v-chip-group>
        </v-col>
      </v-row>
    </v-card-title>

    <v-card-text class="headline">
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
</template>

<script>
export default {
  name: 'Card',
  data() {
    return {
      //아직 article에 없는 값을 임시로 설정
      emotion: '😊',
      tags: ['# 즐거워요', '# 기뻐요', '# 행복해요'],
      recommend: 42
    };
  },
  methods: {
    onClickCard: function() {
      this.$router.replace({
        path: 'ArticleDetail/' + this.article.id
      });
    }
  },
  computed: {
    createdDate: function() {
      const date = new Date(this.article.createdDate);
      const now = new Date();
      const gap = (now.getTime() - date.getTime()) / 1000;
      if (gap > 86400) {
        return (
          date.getFullYear() +
          '년 ' +
          (date.getMonth() + 1) +
          '월 ' +
          date.getDate() +
          '일'
        );
      } else {
        return Math.floor(gap / 3600) + '시간 전';
      }
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
