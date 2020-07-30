import { CREATE_COMMENT } from '@/store/shared/actionTypes';
import CommentService from '@/api/modules/comment';

const state = {};

const getters = {};

const mutations = {};

const actions = {
  // eslint-disable-next-line no-unused-vars
  async [CREATE_COMMENT]({ commit }, comment) {
    return CommentService.create(comment);
  }
};

export default {
  state,
  getters,
  actions,
  mutations
};
