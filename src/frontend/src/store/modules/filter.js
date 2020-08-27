import { SET_FILTER } from '@/store/shared/mutationTypes';
import { SELECT_FILTER } from '@/store/shared/actionTypes';

const state = {
  filter: {
    emotionIds: '',
    isFiltered: false
  }
};

const getters = {
  filter(state) {
    return state.filter;
  }
};

const mutations = {
  [SET_FILTER](state, filter) {
    state.filter = filter;
  }
};

const actions = {
  [SELECT_FILTER]({ commit }, filter) {
    commit(SET_FILTER, filter);
  }
};

export default {
  state,
  getters,
  actions,
  mutations
};
