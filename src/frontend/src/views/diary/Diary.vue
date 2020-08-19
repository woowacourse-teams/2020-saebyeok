<template>
  <div>
    <my-page-tabs></my-page-tabs>
    <emotion-filter v-on:select="readArticles" />
    <div>
      <cards :articles="memberArticles" />
    </div>
    <infinite-loading
      v-if="memberArticles.length"
      :identifier="infiniteId"
      @infinite="infiniteHandler"
      force-use-infinite-wrapper="cards"
      spinner="waveDots"
    >
      <div slot="no-more">모든 글을 다 읽으셨네요 :)</div>
      <div slot="no-result">모든 글을 다 읽으셨네요 :)</div>
    </infinite-loading>
  </div>
</template>

<script>
import MyPageTabs from '@/components/MyPageTabs.vue';
import { mapActions, mapGetters } from 'vuex';
import {
  FETCH_MEMBER_ARTICLES,
  PAGING_MEMBER_ARTICLES,
  FETCH_MEMBER_FILTERING_ARTICLES,
  PAGING_MEMBER_FILTERING_ARTICLES
} from '@/store/shared/actionTypes';
import Cards from '@/components/card/Cards.vue';
import EmotionFilter from './components/EmotionFilter.vue';
import InfiniteLoading from 'vue-infinite-loading';

export default {
  name: 'Diary',
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
    MyPageTabs,
    Cards,
    EmotionFilter,
    InfiniteLoading
  },
  created() {
    this.initMemberArticles();
  },
  computed: {
    ...mapGetters(['memberArticles'])
  },
  methods: {
    ...mapActions([FETCH_MEMBER_ARTICLES]),
    ...mapActions([PAGING_MEMBER_ARTICLES]),
    ...mapActions([FETCH_MEMBER_FILTERING_ARTICLES]),
    ...mapActions([PAGING_MEMBER_FILTERING_ARTICLES]),
    initMemberArticles() {
      this.page = 0;
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
    initMemberFilteringArticles() {
      this.page = 0;
      try {
        this.fetchMemberFilteringArticles({
          page: this.page,
          size: this.size,
          emotionIds: this.emotionIds
        }).then(() => {
          this.page++;
        });
      } catch (error) {
        console.error(error);
      }
    },
    scrollMemberArticles($state) {
      try {
        this.pagingMemberArticles({
          page: this.page,
          size: this.size
        }).then(data => {
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
    },
    scrollMemberFilteringArticles($state) {
      try {
        this.pagingMemberFilteringArticles({
          page: this.page,
          size: this.size,
          emotionIds: this.emotionIds
        }).then(data => {
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
    },
    infiniteHandler($state) {
      setTimeout(() => {
        if (this.isFiltered) {
          this.scrollMemberFilteringArticles($state);
        } else {
          this.scrollMemberArticles($state);
        }
      }, 500);
    },
    async readArticles(emotionIds, isSelectedAll) {
      this.emotionIds = emotionIds.toString();
      this.isFiltered = !isSelectedAll;
      this.initMemberFilteringArticles();
      this.infiniteId += 1;
    }
  },
  props: {
    source: String
  }
};
</script>
