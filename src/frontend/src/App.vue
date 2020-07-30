<template>
  <v-app id="inspire">
    <router-view name="header" />
    <v-main>
      <v-container fluid>
        <router-view />
      </v-container>
    </v-main>
    <router-view name="footer" />
  </v-app>
</template>

<script>
export default {
  components: {},
  props: { source: String }
};
</script>

<style scoped>
#inspire {
  background-color: rgba(227, 214, 244, 0.5);
}
</style>
