import { STATUS } from '../../utils/Status';

const state = {};

const getters = {};

const mutations = {
  catchError(state, error) {
    if (error.response.status === STATUS.UNAUTHORIZED) {
      location.href = '/signin';
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
