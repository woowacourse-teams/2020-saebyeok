<template>
  <v-app id="inspire">
    <Header></Header>
    <v-main>
      <v-container fluid>
        <router-view />
      </v-container>
    </v-main>
    <bottom-navigation />
  </v-app>
</template>

<script>
import Header from './components/Header';
import BottomNavigation from './components/BottomNavigation';

export default {
  components: { Header, BottomNavigation },
  props: { source: String }
};
</script>

<style scoped>
#inspire {
  background-color: rgba(227, 214, 244, 0.5);
}
</style>
