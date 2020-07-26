<template>
  <div>
    <div class="mt-4 overflow-y-auto">
      <div class="w-full" id="article-list">
        <card
                :article="article"
                :key="article.content"
                v-for="article in articles"
        ></card>
      </div>
    </div>
  </div>
</template>

<script>
  import {mapActions, mapGetters} from 'vuex';
  import Card from '@/components/Card.vue';
  import {FETCH_ARTICLES} from '@/store/shared/actionTypes';

  export default {
    name: 'Feed',
    components: {
      Card
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
