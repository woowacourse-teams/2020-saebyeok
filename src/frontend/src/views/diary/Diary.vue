<template>
  <div>
    <my-page-tabs></my-page-tabs>
    <emotion-filter v-on:select="filteringArticles" />
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
import EmotionFilter from '../../components/EmotionFilter.vue';
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
          this.pagingMemberArticles(this.createParams()).then(data => {
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
    filteringArticles(emotionIds, isSelectedAll) {
      this.emotionIds = emotionIds.toString();
      this.isFiltered = !isSelectedAll;
      this.page = 0;

      try {
        this.fetchMemberArticles(this.createParams()).then(() => {
          this.page++;
          this.infiniteId += 1;
        });
      } catch (error) {
        console.error(error);
      }
    }
  },
  props: {
    source: String
  }
};
</script>
