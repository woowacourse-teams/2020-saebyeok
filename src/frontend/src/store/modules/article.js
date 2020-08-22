import {
  SET_ARTICLE,
  SET_ARTICLES,
  ADD_ARTICLES
} from '@/store/shared/mutationTypes';
import {
  FETCH_ARTICLE,
  FETCH_ARTICLES,
  PAGING_ARTICLES,
  CREATE_ARTICLE,
  CLEAR_ARTICLES
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
    return ArticleService.get(articleId).then(({ data }) => {
      commit(SET_ARTICLE, data);
      return data;
    });
  },
  async [FETCH_ARTICLES]({ commit }, params) {
    return ArticleService.getAll(params).then(({ data }) => {
      commit(SET_ARTICLES, data);
      return data;
    });
  },
  async [PAGING_ARTICLES]({ commit }, params) {
    return ArticleService.getAll(params).then(({ data }) => {
      commit(ADD_ARTICLES, data);
      return data;
    });
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
