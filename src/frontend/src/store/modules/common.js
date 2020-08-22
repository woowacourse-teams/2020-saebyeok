const state = {};

const getters = {};

const mutations = {
  catchError(state, error) {
    if (error.response.status === 401) {
      location.href = '/signin';
    }
    if (error.response.status === 500) {
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
