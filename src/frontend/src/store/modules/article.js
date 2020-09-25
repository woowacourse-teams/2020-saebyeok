import {
  ADD_ARTICLES,
  SET_ARTICLE,
  SET_ARTICLES,
  UPDATE_ARTICLE_LIKES
} from '@/store/shared/mutationTypes';
import {
  CLEAR_ARTICLES,
  CREATE_ARTICLE,
  FETCH_ARTICLE,
  FETCH_ARTICLES,
  PAGING_ARTICLES,
  DELETE_ARTICLE,
  LIKE_ARTICLE,
  UNLIKE_ARTICLE
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
  },
  [UPDATE_ARTICLE_LIKES](state, value) {
    state.articles.likesCount += value;
  }
};

const actions = {
  // eslint-disable-next-line no-unused-vars
  async [CREATE_ARTICLE]({ commit }, article) {
    return ArticleService.create(article).catch(error =>
      commit('catchError', error)
    );
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
    return ArticleService.delete(articleId).catch(error =>
      commit('catchError', error)
    );
  },
  [CLEAR_ARTICLES]({ commit }) {
    commit(SET_ARTICLES, []);
  },
  async [LIKE_ARTICLE]({ commit }, articleId) {
    return ArticleService.like(articleId)
      .then(() => commit(UPDATE_ARTICLE_LIKES, 1))
      .catch(error => commit('catchError', error));
  },
  async [UNLIKE_ARTICLE]({ commit }, articleId) {
    return ArticleService.unlike(articleId)
      .then(() => commit(UPDATE_ARTICLE_LIKES, -1))
      .catch(error => commit('catchError', error));
  }
};

export default {
  state,
  getters,
  actions,
  mutations
};
