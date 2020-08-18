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
    },
    // eslint-disable-next-line no-unused-vars
    async readArticles(emotions) {
      //todo : 여기서 emotions를 page, size와 함께 api로 보낸다.
      this.page = 0;
      this.infiniteId += 1;

      await this.fetchMemberArticles({
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
    }
  },
  props: {
    source: String
  }
};
</script>
