<template>
  <v-bottom-navigation app color="#B2A4D4" shift>
    <v-btn to="/feed" height="100%">
      <span>피드</span>
      <v-icon large>mdi-rss</v-icon>
    </v-btn>

    <v-btn to="/emotions" height="100%">
      <span>글쓰기</span>
      <v-icon large>mdi-pencil</v-icon>
    </v-btn>

    <v-btn to="/my-page" height="100%">
      <span>다이어리</span>
      <v-icon large>mdi-note</v-icon>
    </v-btn>
  </v-bottom-navigation>
</template>

<script>
export default {
  name: 'bottom-navigation'
};
</script>

<style></style>
