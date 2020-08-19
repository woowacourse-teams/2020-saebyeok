import {
  ADD_MEMBER_ARTICLES,
  SET_MEMBER_ARTICLE,
  SET_MEMBER_ARTICLES
} from '@/store/shared/mutationTypes';
import {
  FETCH_MEMBER_ARTICLE,
  FETCH_MEMBER_ARTICLES,
  PAGING_MEMBER_ARTICLES
} from '@/store/shared/actionTypes';
import MemberArticleService from '@/api/modules/memberArticle';

const state = {
  memberArticle: {},
  memberArticles: []
};

const getters = {
  memberArticle(state) {
    return state.memberArticle;
  },
  memberArticles(state) {
    return state.memberArticles;
  }
};

const mutations = {
  [SET_MEMBER_ARTICLE](state, memberArticle) {
    state.memberArticle = memberArticle;
  },
  [SET_MEMBER_ARTICLES](state, memberArticles) {
    state.memberArticles = memberArticles;
  },
  [ADD_MEMBER_ARTICLES](state, memberArticles) {
    state.memberArticles = state.memberArticles.concat(memberArticles);
  }
};

const actions = {
  // eslint-disable-next-line no-unused-vars
  async [FETCH_MEMBER_ARTICLE]({ commit }, articleId) {
    return MemberArticleService.get(articleId).then(({ data }) => {
      commit(SET_MEMBER_ARTICLE, data);
      return data;
    });
  },
  async [FETCH_MEMBER_ARTICLES]({ commit }, params) {
    return MemberArticleService.getAll(params).then(({ data }) => {
      commit(SET_MEMBER_ARTICLES, data);
      return data;
    });
  },
  async [PAGING_MEMBER_ARTICLES]({ commit }, params) {
    return MemberArticleService.getAll(params).then(({ data }) => {
      commit(ADD_MEMBER_ARTICLES, data);
      return data;
    });
  }
};

export default {
  state,
  getters,
  actions,
  mutations
};