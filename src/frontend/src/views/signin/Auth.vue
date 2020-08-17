<template>
  <p>인증 중...</p>
</template>

<script>
export default {
  mounted() {
    this.storeAuth();
    location.href = '/feed';
  },
  methods: {
    storeAuth() {
      localStorage.setItem('token', this.getParam('token'));
    },
    getParam(paramName) {
      let params = location.search.substr(location.search.indexOf('?') + 1);
      let extractedToken = '';
      params = params.split('&');
      for (let i = 0; i < params.length; i++) {
        let temp = params[i].split('=');
        if ([temp[0]] == paramName) {
          extractedToken = temp[1].replace('%20', ' ');
        }
      }
      return extractedToken;
    }
  }
};
</script>
