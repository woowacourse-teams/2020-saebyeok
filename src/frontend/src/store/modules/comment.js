import { UPDATE_COMMENT_LIKES } from '@/store/shared/mutationTypes';
import {
  CREATE_COMMENT,
  LIKE_COMMENT,
  UNLIKE_COMMENT
} from '@/store/shared/actionTypes';
import CommentService from '@/api/modules/comment';

const state = {};

const getters = {};

const mutations = {
  [UPDATE_COMMENT_LIKES](state, value) {
    // pros와 vuex구조 및 comments분리 리팩토링 되고 나면 동작 확인하기
    this.comment.value += value;
  }
};

const actions = {
  // eslint-disable-next-line no-unused-vars
  async [CREATE_COMMENT]({ commit }, comment) {
    return CommentService.create(comment);
  },
  async [LIKE_COMMENT]({ commit }, commentId) {
    return CommentService.like(commentId)
      .then(() => commit(UPDATE_COMMENT_LIKES, 1))
      .catch(error => commit('catchError', error));
  },
  async [UNLIKE_COMMENT]({ commit }, commentId) {
    return CommentService.unlike(commentId)
      .then(() => commit(UPDATE_COMMENT_LIKES, commentId, -1))
      .catch(error => commit('catchError', error));
  }
};

export default {
  state,
  getters,
  actions,
  mutations
};
