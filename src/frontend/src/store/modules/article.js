import {
  ADD_ARTICLES,
  SET_ARTICLE,
  SET_ARTICLES
} from '@/store/shared/mutationTypes';
import {
  CLEAR_ARTICLES,
  CREATE_ARTICLE,
  FETCH_ARTICLE,
  FETCH_ARTICLES,
  PAGING_ARTICLES,
  DELETE_ARTICLE
} from '@/store/shared/actionTypes';
import ArticleService from '@/api/modules/article';

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
  },
  [ADD_ARTICLES](state, articles) {
    state.articles = state.articles.concat(articles);
  }
};

const actions = {
  // eslint-disable-next-line no-unused-vars
  async [CREATE_ARTICLE]({ commit }, article) {
    return ArticleService.create(article);
  },
  async [FETCH_ARTICLE]({ commit }, articleId) {
    return ArticleService.get(articleId)
      .then(({ data }) => {
        commit(SET_ARTICLE, data);
        return data;
      })
      .catch(error => commit('catchError', error));
  },
  async [FETCH_ARTICLES]({ commit }, params) {
    return ArticleService.getAll(params)
      .then(({ data }) => {
        commit(SET_ARTICLES, data);
        return data;
      })
      .catch(error => commit('catchError', error));
  },
  async [PAGING_ARTICLES]({ commit }, params) {
    return ArticleService.getAll(params)
      .then(({ data }) => {
        commit(ADD_ARTICLES, data);
        return data;
      })
      .catch(error => commit('catchError', error));
  },
  // eslint-disable-next-line no-unused-vars
  async [DELETE_ARTICLE]({ commit }, articleId) {
    return ArticleService.delete(articleId);
  },
  [CLEAR_ARTICLES]({ commit }) {
    commit(SET_ARTICLES, []);
  }
};

export default {
  state,
  getters,
  actions,
  mutations
};
