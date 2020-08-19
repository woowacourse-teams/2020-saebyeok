<template>
  <div>
    <div class="mt-4 overflow-y-auto">
      <cards :articles="articles" />
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
import { mapActions, mapGetters } from 'vuex';
import { FETCH_ARTICLES, PAGING_ARTICLES } from '@/store/shared/actionTypes';
import Cards from '@/components/card/Cards.vue';
import InfiniteLoading from 'vue-infinite-loading';

export default {
  name: 'Feed',
  data() {
    return {
      page: 0,
      size: 5,
      emotionIds: '',
      infiniteId: +new Date(),
      isFiltered: false
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
    createParams() {
      if (this.isFiltered) {
        return {
          page: this.page,
          size: this.size,
          emotionIds: this.emotionIds
        };
      }
      return {
        page: this.page,
        size: this.size
      };
    },
    infiniteHandler($state) {
      setTimeout(() => {
        try {
          this.pagingArticles(this.createParams()).then(data => {
            if (data.length) {
              this.page++;
              $state.loaded();
            } else {
              $state.complete();
            }
          });
        } catch (error) {
          console.error(error);
        }
      }, 500);
    },
    //todo : 이후 피드의 필터가 구현되면, 이 메서드와 연결해서 사용하면 된다.
    filteringArticles(emotionIds, isSelectedAll) {
      this.emotionIds = emotionIds.toString();
      this.isFiltered = !isSelectedAll;
      this.page = 0;

      try {
        this.fetchArticles(this.createParams()).then(() => {
          this.page++;
          this.infiniteId += 1;
        });
      } catch (error) {
        console.error(error);
      }
    }
  }
};
</script>
