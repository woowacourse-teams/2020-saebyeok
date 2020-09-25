<template>
  <v-app-bar app clipped-right color="#B2A4D4" flat dark>
    <v-app-bar-nav-icon>
      <v-icon @click="$router.go(-1)">mdi-arrow-left</v-icon>
    </v-app-bar-nav-icon>
    <v-spacer></v-spacer>
    <v-toolbar-title>
      <router-link to="/feed" class="title text-h5">새벽</router-link>
    </v-toolbar-title>
    <v-spacer></v-spacer>
    <v-app-bar-nav-icon>
      <logout-button v-if="this.token" />
      <v-icon size="48px" v-else>mdi-blank</v-icon>
    </v-app-bar-nav-icon>
  </v-app-bar>
</template>

<script>
import LogoutButton from '@/components/LogoutButton';

export default {
  name: 'DetailPageHeader',
  data() {
    return { token: localStorage.getItem('token') };
  },
  components: {
    LogoutButton
  }
};
</script>

<style scoped>
.title {
  text-decoration: none;
  color: white;
}
</style>
