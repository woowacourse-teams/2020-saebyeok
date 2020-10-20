import {
  SET_REPORT_CATEGORIES,
  SET_REPORT_TARGET
} from '@/store/shared/mutationTypes';
import {
  FETCH_REPORT_CATEGORIES,
  CREATE_REPORT
} from '@/store/shared/actionTypes';
import ReportService from '@/api/modules/report';

const state = {
  reportCategories: [],
  reportTarget: {}
};

const getters = {
  reportCategories(state) {
    return state.reportCategories;
  },
  reportTarget(state) {
    return state.reportTarget;
  }
};

const mutations = {
  [SET_REPORT_CATEGORIES](state, reportCategories) {
    state.reportCategories = reportCategories;
  },
  [SET_REPORT_TARGET](state, reportTarget) {
    state.reportTarget = reportTarget;
  }
};

const actions = {
  async [FETCH_REPORT_CATEGORIES]({ commit }) {
    return ReportService.getCategories()
      .then(({ data }) => {
        commit(SET_REPORT_CATEGORIES, data);
      })
      .catch(error => commit('catchError', error));
  },
  async [CREATE_REPORT]({ commit }, report) {
    return ReportService.createReport(report).catch(error =>
      commit('catchError', error)
    );
  }
};

export default {
  state,
  getters,
  actions,
  mutations
};
