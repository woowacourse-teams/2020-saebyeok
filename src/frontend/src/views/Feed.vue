<template>
  <div>
    <div class="mt-4 overflow-y-auto">
      <div class="w-full" id="article-list">
        <card
                :article="article"
                :key="article.content"
                v-for="article in this.$store.state.articles"
        ></card>
      </div>
    </div>
  </div>
</template>

<script>
  import Card from '@/components/Card.vue';
  import FeedService from '@/api/modules/feed.js';
  import {mapMutations} from 'vuex';

  export default {
    components: {
      Card
    },
    created() {
      this.getAll();
    },
    methods: {
      ...mapMutations(['setArticles']),
      async getAll() {
        FeedService.getAll()
                .then(response => {
                  this.$store.commit('setArticles', response.data);
                  console.log(response.data);
                  console.log('TWICE IS GREAT');
                  console.log(this.$store.state.articles);
                })
                .catch(error => alert(error))
                .then();
      }
    },
    props: {
      source: String
    }
  };
</script>
