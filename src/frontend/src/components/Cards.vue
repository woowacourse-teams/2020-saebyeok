<template>
  <v-container ma-0 pa-0>
    <v-row dense>
      <v-col v-for="article in articles" :key="article.id" cols="12">
        <card :article="article"></card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import { mapGetters } from 'vuex';
import Card from '@/components/Card.vue';

export default {
  name: 'Cards',
  components: {
    Card
  },
  computed: {
    ...mapGetters(['articles'])
  }
};
</script>
