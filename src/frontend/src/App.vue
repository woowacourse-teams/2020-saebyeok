<template>
  <v-app id="inspire">
    <v-app-bar app clipped-right color="blue-grey" dark>
      <v-app-bar-nav-icon></v-app-bar-nav-icon>
      <v-toolbar-title>Toolbar</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-app-bar-nav-icon></v-app-bar-nav-icon>
    </v-app-bar>

    <v-main>
      <v-container fluid>
        <router-view />
      </v-container>
    </v-main>

    <v-bottom-navigation v-model="bottomNav" dark shift>
      <v-btn to="/feed">
        <span>Feed</span>
        <v-icon>mdi-rss</v-icon>
      </v-btn>

      <v-btn to="/write">
        <span>Write</span>
        <v-icon>mdi-pencil</v-icon>
      </v-btn>

      <v-btn to="/my-page">
        <span>MyPage</span>
        <v-icon>mdi-note</v-icon>
      </v-btn>
    </v-bottom-navigation>
  </v-app>
</template>

<script>
export default {
  props: {
    source: String
  },
  data() {
    return {
      bottomNav: 3
    };
  }
};
</script>
