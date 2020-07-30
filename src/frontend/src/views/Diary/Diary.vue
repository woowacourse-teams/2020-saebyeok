<template>
  <div>
    <my-page-tabs></my-page-tabs>
    <emotion-filter />
    <cards />
  </div>
</template>

<script>
import MyPageTabs from '@/components/MyPageTabs.vue';
import { mapActions, mapGetters } from 'vuex';
import Cards from '@/components/Cards.vue';
import EmotionFilter from './components/EmotionFilter.vue';
import { FETCH_ARTICLES } from '@/store/shared/actionTypes';

export default {
  components: {
    MyPageTabs,
    Cards,
    EmotionFilter
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
