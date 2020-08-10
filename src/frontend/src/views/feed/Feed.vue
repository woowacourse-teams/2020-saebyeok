<template>
  <div>
    <div class="mt-4 overflow-y-auto">
      <cards />
    </div>
    <infinite-loading
      v-if="articles.length"
      @infinite="infiniteHandler"
      force-use-infinite-wrapper="cards"
      spinner="waveDots"
    >
      <div slot="no-more">모든 글을 다 읽으셨네요 :)</div>
    </infinite-loading>
  </div>
</template>

<script>
  import {mapActions, mapGetters} from 'vuex';
  import {FETCH_ARTICLES, PAGING_ARTICLES} from '@/store/shared/actionTypes';
  import Cards from '@/components/card/Cards.vue';
  import InfiniteLoading from 'vue-infinite-loading';

  export default {
    name: 'Feed',
    data() {
      return {
        page: 0,
        size: 5
      };
    },
    components: {
      Cards,
      InfiniteLoading
  },
  created() {
    try {
      this.fetchArticles({
        page: this.page,
        size: this.size
      }).then(() => {
        this.page++;
      });
    } catch (error) {
      console.error(error);
    }
  },
  computed: {
    ...mapGetters(['articles'])
  },
  methods: {
    ...mapActions([FETCH_ARTICLES]),
    ...mapActions([PAGING_ARTICLES]),
    infiniteHandler($state) {
      setTimeout(() => {
        this.pagingArticles({
          page: this.page,
          size: this.size
        })
          .then(data => {
            if (data.length) {
              this.page++;
              $state.loaded();
            } else {
              $state.complete();
            }
          })
          .catch(err => {
            console.error(err);
          });
      }, 500);
    }
  }
};
</script>
