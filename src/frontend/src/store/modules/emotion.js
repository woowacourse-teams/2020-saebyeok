import {
  CATCH_ERROR,
  SET_EMOTION,
  SET_EMOTIONS
} from '@/store/shared/mutationTypes';
import { FETCH_EMOTION, FETCH_EMOTIONS } from '@/store/shared/actionTypes';
import EmotionService from '@/api/modules/emotion';

const state = {
  emotion: {},
  emotions: []
};

const getters = {
  emotion(state) {
    return state.emotion;
  },
  emotions(state) {
    return state.emotions;
  }
};

const mutations = {
  [SET_EMOTION](state, emotion) {
    state.emotion = emotion;
  },
  [SET_EMOTIONS](state, emotions) {
    state.emotions = emotions;
  }
};

const actions = {
  async [FETCH_EMOTION]({ commit }, emotionId) {
    return EmotionService.get(emotionId)
      .then(({ data }) => {
        commit(SET_EMOTION, data);
        return data;
      })
      .catch(error => commit(CATCH_ERROR, error));
  },
  async [FETCH_EMOTIONS]({ commit }) {
    return EmotionService.getAll()
      .then(({ data }) => {
        commit(SET_EMOTIONS, data);
      })
      .catch(error => commit(CATCH_ERROR, error));
  }
};

export default {
  state,
  getters,
  actions,
  mutations
};
