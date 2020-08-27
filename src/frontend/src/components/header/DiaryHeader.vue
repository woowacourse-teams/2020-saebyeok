<template>
  <v-app-bar app clipped-right color="#B2A4D4" flat dark>
    <v-icon size="48px">mdi-blank</v-icon>
    <v-spacer></v-spacer>
    <v-toolbar-title>
      <router-link to="/feed" class="title text-h5">새벽</router-link>
    </v-toolbar-title>
    <v-spacer></v-spacer>
    <v-app-bar-nav-icon>
      <logout-button />
    </v-app-bar-nav-icon>
  </v-app-bar>
</template>

<script>
import LogoutButton from '@/components/LogoutButton';

export default {
  name: 'DiaryHeader',
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
