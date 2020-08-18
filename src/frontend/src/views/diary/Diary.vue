<template>
  <div>
    <my-page-tabs></my-page-tabs>
    <emotion-filter v-on:select="readArticles" />
    <div>
      <cards />
    </div>
    <infinite-loading
      v-if="articles.length"
      :identifier="infiniteId"
      @infinite="infiniteHandler"
      force-use-infinite-wrapper="cards"
      spinner="waveDots"
    >
      <div slot="no-more">모든 글을 다 읽으셨네요 :)</div>
    </infinite-loading>
  </div>
</template>

<script>
import MyPageTabs from '@/components/MyPageTabs.vue';
import { mapActions, mapGetters } from 'vuex';
import { FETCH_ARTICLES, PAGING_ARTICLES } from '@/store/shared/actionTypes';
import Cards from '@/components/card/Cards.vue';
import EmotionFilter from './components/EmotionFilter.vue';
import InfiniteLoading from 'vue-infinite-loading';

export default {
  name: 'Diary',
  data() {
    return {
      page: 0,
      size: 5,
      infiniteId: +new Date()
    };
  },
  components: {
    MyPageTabs,
    Cards,
    EmotionFilter,
    InfiniteLoading
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
    },
    // eslint-disable-next-line no-unused-vars
    readArticles(emotions) {
      //todo : 여기서 emotions를 page, size와 함께 api로 보낸다.
      this.page = 0;
      this.infiniteId += 1;

      setTimeout(() => {
        this.fetchArticles({
          page: this.page,
          size: this.size
        })
          .then(data => {
            if (data.length) {
              this.page++;
            }
          })
          .catch(err => {
            console.error(err);
          });
      }, 500);
    }
  },
  props: {
    source: String
  }
};
</script>
