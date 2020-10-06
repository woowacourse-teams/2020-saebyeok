import {
  ACTIVATE_RECOMMENT,
  DEACTIVATE_RECOMMENT,
  UPDATE_COMMENT_LIKES,
  SET_COMMENTS
} from '@/store/shared/mutationTypes';
import {
  CREATE_COMMENT,
  DELETE_COMMENT,
  LIKE_COMMENT,
  UNLIKE_COMMENT
} from '@/store/shared/actionTypes';

import CommentService from '@/api/modules/comment';

const state = {
  isActiveRecomment: false,
  targetNickname: '',
  comments: []
};

const getters = {
  isActiveRecomment(state) {
    return state.isActiveRecomment;
  },
  targetNickname() {
    return state.targetNickname;
  },
  comments(state) {
    return state.comments;
  }
};

const mutations = {
  [ACTIVATE_RECOMMENT](state, nickname) {
    state.targetNickname = nickname;
    state.isActiveRecomment = true;
  },
  [DEACTIVATE_RECOMMENT](state) {
    state.targetNickname = '';
    state.isActiveRecomment = false;
  },
  [SET_COMMENTS](state, comments) {
    state.comments = comments;
  },
  [UPDATE_COMMENT_LIKES](state, comment) {
    const index = state.comments.findIndex(it => it.id === comment.id);
    state.comments.splice(index, 1, comment);
  }
};

const actions = {
  // eslint-disable-next-line no-unused-vars
  async [CREATE_COMMENT]({ commit }, comment) {
    return CommentService.create(comment).catch(error =>
      commit('catchError', error)
    );
  },
  // eslint-disable-next-line no-unused-vars
  async [DELETE_COMMENT]({ commit }, params) {
    return CommentService.delete(params).catch(error =>
      commit('catchError', error)
    );
  },
  async [LIKE_COMMENT]({ commit, rootGetters }, comment) {
    const currentArticle = rootGetters.article;
    comment.likesCount += 1;
    comment.isLikedByMe = true;

    return CommentService.like({
      articleId: currentArticle.id,
      commentId: comment.id
    })
      .then(() => commit(UPDATE_COMMENT_LIKES, comment))
      .catch(error => commit('catchError', error));
  },
  async [UNLIKE_COMMENT]({ commit, rootGetters }, comment) {
    const currentArticle = rootGetters.article;
    comment.likesCount -= 1;
    comment.isLikedByMe = false;

    return CommentService.unlike({
      articleId: currentArticle.id,
      commentId: comment.id
    })
      .then(() => commit(UPDATE_COMMENT_LIKES, comment))
      .catch(error => commit('catchError', error));
  }
};

export default {
  state,
  getters,
  actions,
  mutations
};
