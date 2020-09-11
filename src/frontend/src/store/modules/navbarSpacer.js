import { SET_NAVBAR_SPACE_NEEDED } from '@/store/shared/mutationTypes';
import { CHECK_NAVBAR_SPACE_NEEDED } from '@/store/shared/actionTypes';

const state = { spaceNeeded: false };

const getters = {
  spaceNeeded(state) {
    return state.spaceNeeded;
  }
};

const mutations = {
  [SET_NAVBAR_SPACE_NEEDED](state, spaceNeeded) {
    state.spaceNeeded = spaceNeeded;
  }
};

const actions = {
  [CHECK_NAVBAR_SPACE_NEEDED]({ commit }) {
    const userAgent = window.navigator.userAgent;
    if (
      (userAgent.search('iPhone') > 0 || userAgent.search('Macintosh') > 0) &&
      (screen.height === 812 ||
        screen.height === 896 ||
        screen.height === 1194 ||
        screen.height === 1366)
    ) {
      commit(SET_NAVBAR_SPACE_NEEDED, true);
    }
  }
};

export default {
  state,
  getters,
  actions,
  mutations
};
