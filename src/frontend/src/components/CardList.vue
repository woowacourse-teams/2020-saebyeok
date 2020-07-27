<template>
  <v-container>
    <v-row dense>
      <v-col :key="article.content" cols="12" v-for="article in articles">
        <card :article="article"></card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import { mapGetters } from 'vuex';
import Card from '@/components/Card.vue';

export default {
  name: 'CardList',
  components: {
    Card
  },
  computed: {
    ...mapGetters(['articles'])
  }
};
</script>
