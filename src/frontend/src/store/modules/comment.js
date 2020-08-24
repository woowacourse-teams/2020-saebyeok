import { CREATE_COMMENT, DELETE_COMMENT } from '@/store/shared/actionTypes';
import CommentService from '@/api/modules/comment';

const state = {};

const getters = {};

const mutations = {};

const actions = {
  // eslint-disable-next-line no-unused-vars
  async [CREATE_COMMENT]({ commit }, comment) {
    return CommentService.create(comment);
  },
  // eslint-disable-next-line no-unused-vars
  async [DELETE_COMMENT]({ commit }, params) {
    return CommentService.delete(params);
  }
};

export default {
  state,
  getters,
  actions,
  mutations
};
