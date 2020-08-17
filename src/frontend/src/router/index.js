import Vue from 'vue';
import VueRouter from 'vue-router';
import Feed from '../views/feed/Feed.vue';
import Post from '../views/post/Post.vue';
import Analysis from '../views/analysis/Analysis.vue';
import Diary from '../views/diary/Diary.vue';
import ArticleDetail from '../views/articleDetail/ArticleDetail.vue';
import Emotions from '../views/emotions/Emotions.vue';
import SignIn from '../views/signin/SignIn.vue';
import Auth from '../views/signin/Auth.vue';
import Header from '@/components/header/Header';
import EmotionsHeader from '@/components/header/EmotionsHeader';
import DetailPageHeader from '@/components/header/DetailPageHeader';

Vue.use(VueRouter);

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    component: SignIn
  },
  {
    path: '/',
    component: () => import('../views/Index.vue'),
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
          main: Feed
        }
      },
      {
        path: 'feed/:articleId',
        components: {
          header: DetailPageHeader,
          main: ArticleDetail
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
          main: Post
        }
      },
      {
        path: 'my-page',
        redirect: 'my-page/analysis'
      },
      {
        path: 'my-page/analysis',
        components: {
          header: Header,
          main: Analysis
        }
      },
      {
        path: 'my-page/diary',
        components: {
          header: Header,
          main: Diary
        }
      }
    ]
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
