import {
  SET_ARTICLES_ANALYSIS,
  SET_COMMENTS_ANALYSIS
} from '@/store/shared/mutationTypes';
import {
  FETCH_ARTICLES_ANALYSIS,
  FETCH_COMMENTS_ANALYSIS
} from '@/store/shared/actionTypes';
import AnalysisService from '@/api/modules/analysis';

const state = {
  articlesAnalysis: {},
  commentsAnalysis: {}
};

const getters = {
  articlesAnalysis(state) {
    return state.articlesAnalysis;
  },
  commentsAnalysis(state) {
    return state.commentsAnalysis;
  }
};

const mutations = {
  [SET_ARTICLES_ANALYSIS](state, articlesAnalysis) {
    state.articlesAnalysis = articlesAnalysis;
  },
  [SET_COMMENTS_ANALYSIS](state, commentsAnalysis) {
    state.commentsAnalysis = commentsAnalysis;
  }
};

const actions = {
  async [FETCH_ARTICLES_ANALYSIS]({ commit }) {
    return AnalysisService.getArticlesAnalysis().then(({ data }) => {
      commit(SET_ARTICLES_ANALYSIS, data);
    });
  },
  async [FETCH_COMMENTS_ANALYSIS]({ commit }) {
    return AnalysisService.getCommentsAnalysis().then(({ data }) => {
      commit(SET_COMMENTS_ANALYSIS, data);
    });
  }
};

export default {
  state,
  getters,
  actions,
  mutations
};
