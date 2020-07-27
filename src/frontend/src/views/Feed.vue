<template>
  <div>
    <div class="mt-4 overflow-y-auto">
      <cards />
    </div>
  </div>
</template>

<script>
import { mapActions, mapGetters } from 'vuex';
import Cards from '@/components/Cards.vue';
import { FETCH_ARTICLES } from '@/store/shared/actionTypes';

export default {
  name: 'Feed',
  components: {
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
