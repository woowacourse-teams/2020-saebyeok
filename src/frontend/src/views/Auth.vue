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
    getParam(sname) {
      var params = location.search.substr(location.search.indexOf('?') + 1);
      var sval = '';
      params = params.split('&');
      for (var i = 0; i < params.length; i++) {
        let temp = params[i].split('=');
        if ([temp[0]] == sname) {
          sval = temp[1].replace('%20', ' ');
        }
      }
      return sval;
    }
  }
};
</script>
