import Vue from 'vue';
import Vuex from 'vuex';
import Article from '@/store/modules/article';
import Comment from '@/store/modules/comment';
import Emotion from '@/store/modules/emotion';
import MemberArticle from '@/store/modules/memberArticle';
import Filter from '@/store/modules/filter';
import Analysis from '@/store/modules/analysis';
import Common from '@/store/modules/common';
import Snackbar from '@/store/modules/snackbar';
import NavbarSpacer from '@/store/modules/navbarSpacer';
import RequestLoginModal from '@/store/modules/requestLoginModal';
import Report from '@/store/modules/report';

Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    Article,
    Comment,
    Emotion,
    MemberArticle,
    Filter,
    Analysis,
    Common,
    Snackbar,
    NavbarSpacer,
    Report,
    RequestLoginModal
  }
});
