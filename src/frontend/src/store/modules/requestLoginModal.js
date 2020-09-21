import {
  SHOW_REQUEST_LOGIN_MODAL,
  HIDE_REQUEST_LOGIN_MODAL
} from '@/store/shared/mutationTypes';

const state = {
  isShowRequestLoginModal: false
};

const getters = {
  isShowRequestLoginModal(state) {
    return state.isShowRequestLoginModal;
  }
};

const mutations = {
  [SHOW_REQUEST_LOGIN_MODAL](state) {
    state.isShowRequestLoginModal = true;
  },
  [HIDE_REQUEST_LOGIN_MODAL](state) {
    state.isShowRequestLoginModal = false;
  }
};

export default {
  state,
  getters,
  mutations
};
