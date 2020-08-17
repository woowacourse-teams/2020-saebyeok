<template>
  <div>
    <my-page-tabs></my-page-tabs>
    <emotion-filter />
    <div>
      <cards :articles="memberArticles" />
    </div>
    <infinite-loading
      v-if="memberArticles.length"
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
import {
  FETCH_MEMBER_ARTICLES,
  PAGING_MEMBER_ARTICLES
} from '@/store/shared/actionTypes';
import Cards from '@/components/card/Cards.vue';
import EmotionFilter from './components/EmotionFilter.vue';
import InfiniteLoading from 'vue-infinite-loading';

export default {
  name: 'Diary',
  data() {
    return {
      page: 0,
      size: 5
    };
  },
  components: {
    MyPageTabs,
    Cards,
    EmotionFilter,
    InfiniteLoading
  },
  created() {
    try {
      this.fetchMemberArticles({
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
    ...mapGetters(['memberArticles'])
  },
  methods: {
    ...mapActions([FETCH_MEMBER_ARTICLES]),
    ...mapActions([PAGING_MEMBER_ARTICLES]),
    infiniteHandler($state) {
      setTimeout(() => {
        this.pagingMemberArticles({
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
  },
  props: {
    source: String
  }
};
</script>
