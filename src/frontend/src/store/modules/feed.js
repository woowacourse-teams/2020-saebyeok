import { SET_ARTICLE, SET_ARTICLES } from '@/store/shared/mutationTypes';
import { FETCH_ARTICLE, FETCH_ARTICLES } from '@/store/shared/actionTypes';
import ArticleService from '@/api/modules/feed';

const state = {
  article: {},
  articles: []
};

const getters = {
  article(state) {
    return state.article;
  },
  articles(state) {
    return state.articles;
  }
};

const mutations = {
  [SET_ARTICLE](state, article) {
    state.article = article;
  },
  [SET_ARTICLES](state, articles) {
    state.articles = articles;
  }
};

const actions = {
  async [FETCH_ARTICLE]({ commit }, articleId) {
    return ArticleService.get(articleId).then(({ data }) => {
      commit(SET_ARTICLE, data);
      return data;
    });
  },
  async [FETCH_ARTICLES]({ commit }) {
    return ArticleService.getAll().then(({ data }) => {
      commit(SET_ARTICLES, data);
    });
  }
};

export default {
  state,
  getters,
  actions,
  mutations
};
