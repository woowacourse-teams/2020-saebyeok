<template>
  <div>
    <div class="mt-4 overflow-y-auto">
      <!-- <div class="w-full" id="article-list">
        <card
          :article="article"
          :key="article.content"
          v-for="article in articles"
        ></card>
      </div> -->
      <card-list />
    </div>
  </div>
</template>

<script>
import { mapActions, mapGetters } from 'vuex';
import CardList from '@/components/CardList.vue';
import { FETCH_ARTICLES } from '@/store/shared/actionTypes';

export default {
  name: 'Feed',
  components: {
    CardList
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
