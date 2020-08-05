<template>
  <v-container>
    <v-row dense>
      <v-col v-for="article in articles" :key="article.id" cols="12">
        <card :article="article"></card>
      </v-col>
    </v-row>
    <infinite-loading
      v-if="articles.length"
      @infinite="infiniteHandler"
      force-use-infinite-wrapper="v-row"
      spinner="waveDots"
    >
    </infinite-loading>
  </v-container>
</template>

<script>
import Card from '@/components/Card.vue';
import axios from 'axios';
import InfiniteLoading from 'vue-infinite-loading';

export default {
  name: 'Cards',
  data() {
    return {
      articles: [],
      page: 1
    };
  },
  created() {
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
  components: {
    Card,
    InfiniteLoading
  },
  methods: {
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
  }
};
</script>
