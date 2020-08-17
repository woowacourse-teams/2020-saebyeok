<template>
  <v-container fill-height style="background-color: red;">
    <v-layout column align-center>
      <v-flex class="ma-16">
        <h1>새벽</h1>
      </v-flex>
      <v-flex>
        <v-btn
          color="success"
          dark
          class="ma-16 px-8"
          href="/oauth2/authorization/naver"
          >NAVER로 로그인</v-btn
        >
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
export default {};
</script>

<style></style>
