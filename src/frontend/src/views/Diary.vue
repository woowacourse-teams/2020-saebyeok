<template>
  <div>
    <my-page-tabs></my-page-tabs>

    <v-card class="mx-auto rounded-lg" color="#faf9f5" max-width="400">
      <v-row>
        <v-col
          cols="3"
          style="font-size: 40px; text-align: center; line-height: 40px;"
        >
          ALL
        </v-col>
        <v-col
          cols="9"
          style="font-size: 40px; text-align: center; line-height: 40px;"
          >😄😭😩😍😊😂</v-col
        >
      </v-row>
    </v-card>
    <cards />
  </div>
</template>

<script>
import MyPageTabs from '@/components/MyPageTabs.vue';
import { mapActions, mapGetters } from 'vuex';
import Cards from '@/components/Cards.vue';
import { FETCH_ARTICLES } from '@/store/shared/actionTypes';

export default {
  components: {
    MyPageTabs,
    Cards
  },
  created() {
    this.fetchArticles();
  },
  computed: {
    ...mapGetters(['articles'])
  },
  methods: {
    ...mapActions([FETCH_ARTICLES])
  },
  props: {
    source: String
  }
};
</script>
