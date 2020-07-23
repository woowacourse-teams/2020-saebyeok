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

    <bottom-navigation />
  </v-app>
</template>

<script>
import BottomNavigation from './components/BottomNavigation';
export default {
  components: {
    BottomNavigation
  },
  props: {
    source: String
  }
};
</script>
