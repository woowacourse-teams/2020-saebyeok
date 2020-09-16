import { SET_REPORT_CATEGORIES } from '@/store/shared/mutationTypes';
import { FETCH_REPORT_CATEGORIES } from '@/store/shared/actionTypes';
import ReportService from '@/api/modules/report';

const state = {
  reportCategories: []
};

const getters = {
  reportCategories(state) {
    return state.reportCategories;
  }
};

const mutations = {
  [SET_REPORT_CATEGORIES](state, reportCategories) {
    state.reportCategories = reportCategories;
  }
};

const actions = {
  async [FETCH_REPORT_CATEGORIES]({ commit }) {
    return ReportService.getCategories().then(({ data }) => {
      commit(SET_REPORT_CATEGORIES, data);
    });
  }
};

export default {
  state,
  getters,
  actions,
  mutations
};
