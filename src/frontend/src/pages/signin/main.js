import Vue from 'vue';
import App from './App.vue';
import router from './router.js';
import store from '../../store';
import vuetify from '../../plugins/vuetify';

Vue.config.productionTip = false;

alert('signin의 main.js 로딩됨');

new Vue({
  router,
  store,
  vuetify,
  render: h => h(App)
}).$mount('#app');
