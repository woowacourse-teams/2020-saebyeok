<template>
  <v-bottom-navigation v-model="bottomNav" dark shift>
    <v-btn to="/feed" height="100%">
      <span>Feed</span>
      <v-icon>mdi-rss</v-icon>
    </v-btn>

    <v-btn to="/write" height="100%">
      <span>Write</span>
      <v-icon>mdi-pencil</v-icon>
    </v-btn>

    <v-btn to="/my-page" height="100%">
      <span>MyPage</span>
      <v-icon>mdi-note</v-icon>
    </v-btn>
  </v-bottom-navigation>
</template>

<script>
export default {
  name: 'bottom-navigation',
  data() {
    return {
      bottomNav: 3
    };
  }
};
</script>
