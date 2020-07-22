import Vue from 'vue';
import VueRouter from 'vue-router';
import Feed from '../views/Feed.vue';
import Write from '../views/Write.vue';
import MyPage from '../views/MyPage.vue';

Vue.use(VueRouter);

const routes = [
  {
    path: '/feed',
    name: 'Feed',
    component: Feed
  },
  {
    path: '/write',
    name: 'Write',
    component: Write
  },
  {
    path: '/my-page',
    name: 'MyPage',
    component: MyPage
  }
];

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
});

export default router;
