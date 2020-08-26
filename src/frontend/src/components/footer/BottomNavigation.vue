<template>
  <div>
    <v-bottom-navigation
      app
      color="#B2A4D4"
      shift
      :class="{ iphoneX: isiPhoneX }"
    >
      <v-btn to="/feed" height="100%">
        <span>피드</span>
        <v-icon large>mdi-rss</v-icon>
      </v-btn>

      <v-btn to="/emotions" height="100%">
        <span>글쓰기</span>
        <v-icon large>mdi-pencil</v-icon>
      </v-btn>

      <v-btn to="/my-page" height="100%">
        <span>다이어리</span>
        <v-icon large>mdi-note</v-icon>
      </v-btn>
    </v-bottom-navigation>
  </div>
</template>

<script>
export default {
  name: 'bottom-navigation',
  data() {
    return {
      isiPhoneX: false
    };
  },
  mounted() {
    this.checkUserAgent();
  },
  methods: {
    checkUserAgent() {
      const userAgent = window.navigator.userAgent;
      if (
        (userAgent.search('iPhone') > 0 || userAgent.search('Macintosh') > 0) &&
        (screen.height === 812 ||
          screen.height === 896 ||
          screen.height === 1194 ||
          screen.height === 1366)
      ) {
        this.isiPhoneX = true;
      }
    }
  }
};
</script>

<style>
.iphoneX {
  height: 76px !important;
  padding-bottom: 20px;
}
</style>
