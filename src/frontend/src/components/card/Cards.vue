<template>
  <v-container pa-0>
    <div
      style="margin-top: 20%; text-align: center; line-height: 200%"
      v-if="isNoArticles"
    >
      {{ noArticlesMessage }}
      <br />
      새벽에 이야기를 들려주시면 좋겠어요 :)
    </div>
    <v-row dense>
      <v-col v-for="article in articles" :key="article.id" cols="12">
        <card
          :article="article"
          @clickCardContent="onClickCard(article)"
        ></card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import Card from '@/components/card/Card.vue';

export default {
  name: 'Cards',
  data() {
    return {
      isNoArticles: false
    };
  },
  updated() {
    if (this.articles.length === 0 && !this.isFiltered) {
      this.isNoArticles = true;
    }
  },
  methods: {
    onClickCard: function(article) {
      this.$router.push({
        path: this.$router.history.current.path + '/' + article.id
      });
    }
  },
  components: {
    Card
  },
  props: {
    articles: {
      type: Array,
      required: true
    },
    noArticlesMessage: String,
    isFiltered: Boolean
  }
};
</script>
