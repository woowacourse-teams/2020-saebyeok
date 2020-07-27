<template>
  <v-card class="mx-auto" color="#faf9f5" max-width="400">
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
        <v-col align="right" cols="10" justify="end">
          <v-chip-group
            active-class="black--text text--accent-4"
            column
            multiple
          >
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
          <v-col align="left" cols="6" justify="end">
            <v-icon class="mr-1">mdi-hand-heart</v-icon>
            <span class="subheading mr-2">{{ recommand }}</span>
          </v-col>
          <v-col align="right" cols="4" justigy="end">
            <div v-if="article.isCommentAllowed">
              <v-icon class="mr-1">mdi-comment</v-icon>
              <span class="subheading">{{ article.comments.length }}</span>
            </div>
          </v-col>
          <v-col align="right" cols="2" justigy="end">
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
      recommand: 42
    };
  },
  computed: {
    createdDate: function() {
      let date = new Date(this.article.createdDate);
      let now = new Date();
      let gap = (now.getTime() - date.getTime()) / 1000;
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
