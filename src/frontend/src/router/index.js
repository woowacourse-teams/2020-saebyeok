import Vue from 'vue';
import VueRouter from 'vue-router';
import Feed from '../views/Feed/Feed.vue';
import Post from '../views/Post/Post.vue';
import Analysis from '../views/Analysis/Analysis.vue';
import Diary from '../views/Diary/Diary.vue';
import ArticleDetail from '../views/ArticleDetail/ArticleDetail.vue';
import Emotions from '../views/Emotions/Emotions.vue';
import Header from '@/components/Header';
import EmotionsHeader from '@/components/EmotionsHeader';
import BottomNavigation from '@/components/BottomNavigation';
import DetailPageHeader from '@/components/DetailPageHeader';

Vue.use(VueRouter);

const routes = [
  {
    path: '/feed',
    name: 'Feed',
    components: {
      default: Feed,
      header: Header,
      footer: BottomNavigation
    }
  },
  {
    path: '/feed/:articleId',
    name: 'ArticleDetail',
    components: {
      default: ArticleDetail,
      header: DetailPageHeader,
      footer: BottomNavigation
    }
  },
  {
    path: '/emotions',
    name: 'Emotions',
    components: {
      default: Emotions,
      header: EmotionsHeader
    }
  },
  {
    path: '/post/:emotionId',
    name: 'Post',
    components: {
      default: Post,
      header: EmotionsHeader,
      footer: BottomNavigation
    }
  },
  {
    path: '/my-page',
    redirect: '/my-page/analysis'
  },
  {
    path: '/my-page/analysis',
    name: 'Analysis',
    components: {
      default: Analysis,
      header: Header,
      footer: BottomNavigation
    }
  },
  {
    path: '/my-page/diary',
    name: 'Diary',
    components: {
      default: Diary,
      header: Header,
      footer: BottomNavigation
    }
  },
  {
    path: '/',
    redirect: '/feed'
  }
];

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes,
  // eslint-disable-next-line no-unused-vars
  scrollBehavior(to, from, savedPosition) {
    return { x: 0, y: 0 };
  }
});

export default router;
