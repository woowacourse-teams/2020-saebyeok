import Vue from 'vue';
import axios from 'axios';
import VueAxios from 'vue-axios';
const ApiService = {
  init() {
    Vue.use(VueAxios, axios);
  },
  get(uri) {
    return Vue.axios.get(`${uri}`, {
      headers: {
        // Authorization: `Bearer ${localStorage.getItem('token')}` || ''
      }
    });
  },
  login(uri, config) {
    return Vue.axios.post(`${uri}`, {}, config);
  },
  post(uri, params) {
    return Vue.axios.post(`${uri}`, params, {
      headers: {
        // Authorization: `Bearer ${localStorage.getItem('token')}` || ''
      }
    });
  },
  update(uri, params) {
    return Vue.axios.put(uri, params, {
      headers: {
        // Authorization: `Bearer ${localStorage.getItem('token')}` || ''
      }
    });
  },
  delete(uri) {
    return Vue.axios.delete(uri, {
      headers: {
        // Authorization: `Bearer ${localStorage.getItem('token')}` || ''
      }
    });
  }
};
ApiService.init();
export default ApiService;
