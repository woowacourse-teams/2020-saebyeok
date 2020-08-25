<template>
  <v-app>
    <v-layout column align-center fill-height class="background">
      <v-flex class="ma-16 pt-16">
        <p>새벽</p>
      </v-flex>
      <v-flex>
        <v-btn
          color="success"
          dark
          large
          class="ma-16 px-8"
          href="/oauth2/authorization/naver"
          >NAVER로 로그인</v-btn
        >
      </v-flex>
    </v-layout>
  </v-app>
</template>

<script>
export default {};
</script>

<style scoped>
.background {
  background-image: url('/img/dawnTezel.jpg');
  background-position: center center;
  background-size: cover;
}
p {
  font-size: 40px;
  color: white;
}
</style>
