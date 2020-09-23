<template>
  <div>
    <my-page-tabs></my-page-tabs>
    <emotion-filter class="ma-3" />
    <div>
      <cards
        :articles="memberArticles"
        :noArticlesMessage="this.noArticlesMessage"
        :isFiltered="this.isFiltered"
      />
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
  PAGING_MEMBER_ARTICLES,
  CLEAR_MEMBER_ARTICLES
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
      isFiltered: false,
      noArticlesMessage: '아직 작성한 글이 없네요~'
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
    ...mapGetters(['memberArticles']),
    ...mapGetters(['filter'])
  },
  methods: {
    ...mapActions([FETCH_MEMBER_ARTICLES]),
    ...mapActions([PAGING_MEMBER_ARTICLES]),
    ...mapActions([CLEAR_MEMBER_ARTICLES]),
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
      if (this.isFiltered && this.emotionIds.length === 0) {
        $state.complete();
        return;
      }
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
    }
  },
  watch: {
    filter: function() {
      this.emotionIds = this.filter.emotionIds.toString();
      this.isFiltered = this.filter.isFiltered;
      this.page = 0;

      if (this.isFiltered && this.emotionIds.length === 0) {
        this.clearMemberArticles();
        return;
      }

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
