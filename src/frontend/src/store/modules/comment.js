import {
  CATCH_ERROR,
  ACTIVATE_RECOMMENT,
  DEACTIVATE_RECOMMENT,
  UPDATE_COMMENT_LIKES,
  SET_COMMENTS
} from '@/store/shared/mutationTypes';
import {
  CREATE_COMMENT,
  DELETE_COMMENT,
  FETCH_COMMENTS,
  LIKE_COMMENT,
  UNLIKE_COMMENT
} from '@/store/shared/actionTypes';

import CommentService from '@/api/modules/comment';

const state = {
  isActiveRecomment: false,
  targetNickname: '',
  targetCommentId: null,
  comments: []
};

const getters = {
  isActiveRecomment(state) {
    return state.isActiveRecomment;
  },
  targetNickname() {
    return state.targetNickname;
  },
  targetCommentId() {
    return state.targetCommentId;
  },
  comments(state) {
    return state.comments;
  }
};

const mutations = {
  [UPDATE_COMMENT_LIKES](state, value) {
    // pros와 vuex구조 및 comments분리 리팩토링 되고 나면 동작 확인하기
    // this.comment.value += value;
    console.log(state, value);
  },
  [ACTIVATE_RECOMMENT](state, payload) {
    state.targetNickname = payload.targetNickname;
    state.targetCommentId = payload.targetCommentId;
    state.isActiveRecomment = true;
  },
  [DEACTIVATE_RECOMMENT](state) {
    state.targetNickname = '';
    state.targetCommentId = null;
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
      commit(CATCH_ERROR, error)
    );
  },
  // eslint-disable-next-line no-unused-vars
  async [DELETE_COMMENT]({ commit }, params) {
    return CommentService.delete(params).catch(error =>
      commit(CATCH_ERROR, error)
    );
  },
  async [FETCH_COMMENTS]({ commit }, articleId) {
    return CommentService.get(articleId)
      .then(({ data }) => {
        commit(SET_COMMENTS, data);
        return data;
      })
      .catch(error => commit(CATCH_ERROR, error));
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
      .catch(error => commit(CATCH_ERROR, error));
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
      .catch(error => commit(CATCH_ERROR, error));
  }
};

export default {
  state,
  getters,
  actions,
  mutations
};
