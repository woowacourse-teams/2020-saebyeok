<template>
  <v-app-bar app clipped-right color="#f5f5f5" flat>
    <v-app-bar-nav-icon>
      <v-icon @click="$router.go(-1)">mdi-arrow-left</v-icon>
    </v-app-bar-nav-icon>
  </v-app-bar>
</template>

<script>
export default {};
</script>
