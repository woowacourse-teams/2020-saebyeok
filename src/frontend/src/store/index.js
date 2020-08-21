import Vue from 'vue';
import Vuex from 'vuex';
import Article from '@/store/modules/article';
import Comment from '@/store/modules/comment';
import Emotion from '@/store/modules/emotion';
import MemberArticle from '@/store/modules/memberArticle';
import Filter from '@/store/modules/filter';

Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    Article,
    Comment,
    Emotion,
    MemberArticle,
    Filter
  }
});
