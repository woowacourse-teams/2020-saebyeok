<template>
  <v-app-bar app clipped-right color="#B2A4D4" flat dark>
    <v-app-bar-nav-icon>
      <v-icon large @click="$router.go(-1)">mdi-arrow-left</v-icon>
    </v-app-bar-nav-icon>
    <v-spacer></v-spacer>
    <v-toolbar-title>
      <span class="title text-h4">새벽</span>
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
  name: 'DetailPageHeader',
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
