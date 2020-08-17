import Vue from 'vue';
import VueRouter from 'vue-router';
import SignIn from './SignIn.vue';

Vue.use(VueRouter);

alert('라우터 로딩됨');

const routes = [
  // {
  //   path: '/',
  //   redirect: '/sign-in'
  // },
  {
    path: '/signin',
    name: 'SignIn',
    component: SignIn
  }
];

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
});

export default router;
