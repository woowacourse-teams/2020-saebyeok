<template>
  <v-container pa-0>
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
    }
  }
};
</script>
