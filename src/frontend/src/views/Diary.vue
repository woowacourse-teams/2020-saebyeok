<template>
  <div>
    <my-page-tabs></my-page-tabs>

    <v-card class="mx-auto rounded-lg" color="#faf9f5" max-width="400">
      <v-row>
        <v-col
          style="font-size: 40px; text-align: center; line-height: 40px; padding: 10px 0px 12px 0px; "
        >
          <v-chip>
            All
          </v-chip>
        </v-col>
        <v-col
          v-for="emotion in emotions"
          :key="emotion"
          style="font-size: 40px; text-align: center; line-height: 40px; padding: 12px 0px 12px 0px;"
          >{{ emotion }}</v-col
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
  data() {
    return {
      emotions: ['😄', '😭', '😩', '😍', '😊', '😂']
    };
  },
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
