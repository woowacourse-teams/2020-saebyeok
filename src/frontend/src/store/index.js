import Vue from 'vue';
import Vuex from 'vuex';
import Article from '@/store/modules/article';
import Comment from '@/store/modules/comment';

Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    Article,
    Comment
  }
});
