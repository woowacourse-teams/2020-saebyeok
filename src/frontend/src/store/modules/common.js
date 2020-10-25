import { CATCH_ERROR } from '@/store/shared/mutationTypes';
import { STATUS } from '../../utils/Status';

const state = {};

const getters = {};

const mutations = {
  [CATCH_ERROR](state, error) {
    if (error.response.status === STATUS.UNAUTHORIZED) {
      this.commit('showRequestLoginModal');
    }
    if (error.response.status === STATUS.INTERNAL_SERVER_ERROR) {
      location.href = '/error';
    }
  }
};

const actions = {};

export default {
  state,
  getters,
  actions,
  mutations
};
