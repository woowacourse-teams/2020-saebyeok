import Vue from 'vue';
import VueRouter from 'vue-router';
import Feed from '../views/Feed.vue';
import Post from '../views/Post.vue';
import Analysis from '../views/Analysis/Analysis.vue';
import Diary from '../views/Diary/Diary.vue';

import Header from '@/components/Header';
import BottomNavigation from '@/components/BottomNavigation';

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
    path: '/post',
    name: 'Post',
    components: {
      default: Post,
      header: Header,
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
  routes
});

export default router;
