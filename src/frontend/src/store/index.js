import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    articles: []
  },
  mutations: {
    setArticles: (state, payload) => {
      state.articles = payload;
    }
  },
  actions: {},
  modules: {}
});
