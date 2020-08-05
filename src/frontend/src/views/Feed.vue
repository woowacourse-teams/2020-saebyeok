<template>
  <div>
    <div class="mt-4 overflow-y-auto">
      <cards :articles="articles" />
    </div>
    <infinite-loading
      v-if="articles.length"
      @infinite="infiniteHandler"
      force-use-infinite-wrapper="cards"
      spinner="waveDots"
    >
    </infinite-loading>
  </div>
</template>

<script>
import { mapGetters } from 'vuex';
import Cards from '@/components/Cards.vue';
// import { FETCH_ARTICLES } from '@/store/shared/actionTypes';
import axios from 'axios';
import InfiniteLoading from 'vue-infinite-loading';

export default {
  name: 'Feed',
  data() {
    return {
      articles: [],
      page: 1
    };
  },
  components: {
    Cards,
    InfiniteLoading
  },
  created() {
    // this.fetchArticles();
    async function getArticlesFromApi() {
      try {
        const init = await axios.get(`/api/articles`, {
          params: {
            page: 0,
            size: 5
          }
        });
        // const data = await init.json();
        // return data;
        console.log('TWICE   ', init.data);
        return init.data;
      } catch (error) {
        console.error(error);
      }
    }

    getArticlesFromApi().then(data => {
      this.articles = data;
    });
  },
  computed: {
    ...mapGetters(['articles'])
  },
  methods: {
    // ...mapActions([FETCH_ARTICLES])
    infiniteHandler($state) {
      axios
        .get(`/api/articles`, {
          params: {
            page: this.page,
            size: 5
          }
        })
        .then(({ data }) => {
          console.log('OHMYGIRL ', data);
          setTimeout(() => {
            if (data.length) {
              this.page++;
              this.articles = this.articles.concat(data);
              $state.loaded();
              console.log('after : ', this.articles.length, this.page);
            } else {
              $state.complete();
            }
          }, 1000);
        })
        .catch(err => {
          console.error(err);
        });
    }
  },
  props: {
    source: String
  }
};
</script>
