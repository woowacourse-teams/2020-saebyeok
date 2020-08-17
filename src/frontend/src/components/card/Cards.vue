<template>
  <v-container>
    <v-row dense>
      <v-col v-for="article in articles" :key="article.id" cols="12">
        <card :article="article"></card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import Card from '@/components/card/Card.vue';

export default {
  name: 'Cards',
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
