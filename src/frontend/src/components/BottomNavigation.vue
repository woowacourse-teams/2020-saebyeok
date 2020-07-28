<template>
  <v-bottom-navigation v-model="bottomNav" app color="#B2A4D4" shift>
    <v-btn to="/feed" height="100%">
      <span>Feed</span>
      <v-icon large>mdi-rss</v-icon>
    </v-btn>

    <v-btn to="/post" height="100%">
      <span>Write</span>
      <v-icon large>mdi-pencil</v-icon>
    </v-btn>

    <v-btn to="/my-page" height="100%">
      <span>MyPage</span>
      <v-icon large>mdi-note</v-icon>
    </v-btn>
  </v-bottom-navigation>
</template>

<script>
export default {
  name: 'bottom-navigation'
};
</script>
