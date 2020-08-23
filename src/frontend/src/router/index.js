import Vue from 'vue';
import VueRouter from 'vue-router';
import Index from '../views/Index.vue';
import Feed from '../views/feed/Feed.vue';
import Post from '../views/post/Post.vue';
import Analysis from '../views/analysis/Analysis.vue';
import Diary from '../views/diary/Diary.vue';
import DiaryDetail from '../views/diary/DiaryDetail.vue';
import ArticleDetail from '../views/articleDetail/ArticleDetail.vue';
import Emotions from '../views/emotions/Emotions.vue';
import SignIn from '../views/signin/SignIn.vue';
import Auth from '../views/signin/Auth.vue';
import Header from '@/components/header/Header';
import EmotionsHeader from '@/components/header/EmotionsHeader';
import DetailPageHeader from '@/components/header/DetailPageHeader';
import DiaryHeader from '@/components/header/DiaryHeader';
import BottomNavigation from '@/components/footer/BottomNavigation';
import ErrorPage from '@/views/ErrorPage.vue';
import NotFoundPage from '@/views/NotFoundPage.vue';

Vue.use(VueRouter);

const routes = [
  {
    path: '/',
    redirect: '/feed'
  },
  {
    path: '/signin',
    component: SignIn
  },
  {
    path: '/',
    component: Index,
    children: [
      {
        path: 'auth',
        components: {
          main: Auth
        }
      },
      {
        path: 'feed',
        name: 'Feed',
        components: {
          header: Header,
          main: Feed,
          footer: BottomNavigation
        }
      },
      {
        path: 'feed/:articleId',
        components: {
          header: DetailPageHeader,
          main: ArticleDetail,
          footer: BottomNavigation
        }
      },
      {
        path: 'emotions',
        components: {
          header: EmotionsHeader,
          main: Emotions
        }
      },
      {
        path: 'post/:emotionId',
        components: {
          header: EmotionsHeader,
          main: Post,
          footer: BottomNavigation
        }
      },
      {
        path: 'my-page',
        redirect: 'my-page/analysis'
      },
      {
        path: 'my-page/analysis',
        components: {
          header: DiaryHeader,
          main: Analysis,
          footer: BottomNavigation
        }
      },
      {
        path: 'my-page/diary',
        components: {
          header: DiaryHeader,
          main: Diary,
          footer: BottomNavigation
        }
      },
      {
        path: 'my-page/diary/:articleId',
        components: {
          header: DetailPageHeader,
          main: DiaryDetail,
          footer: BottomNavigation
        }
      }
    ]
  },
  {
    path: '*',
    redirect: '/404'
  },
  {
    path: '/404',
    component: NotFoundPage
  },
  {
    path: '/error',
    component: ErrorPage
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
