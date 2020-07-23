import Vue from 'vue';
import VueRouter from 'vue-router';
import Feed from '../views/Feed.vue';
import Post from '../views/Post.vue';
import Analysis from '../views/Analysis.vue';
import Diary from '../views/Diary.vue';

Vue.use(VueRouter);

const routes = [
  {
    path: '/feed',
    name: 'Feed',
    component: Feed
  },
  {
    path: '/post',
    name: 'Post',
    component: Post
  },
  {
    path: '/my-page',
    redirect: '/my-page/analysis'
  },
  {
    path: '/my-page/analysis',
    name: 'Analysis',
    component: Analysis
  },
  {
    path: '/my-page/diary',
    name: 'Diary',
    component: Diary
  }
];

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
});

export default router;
