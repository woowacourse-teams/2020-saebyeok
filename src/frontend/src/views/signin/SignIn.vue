<template>
  <v-app>
    <v-layout column align-center fill-height class="background">
      <v-flex class="ma-16 pt-16">
        <p>새벽</p>
      </v-flex>
      <v-flex>
        <a href="/oauth2/authorization/naver">
          <v-img
            class="naver-button"
            alt="네이버 아이디로 로그인"
            width="194px"
            src="/img/naverLogin.png"
        /></a>
        <a href="/oauth2/authorization/google">
          <v-img
            class="google-button"
            alt="Google로 로그인"
            width="198px"
            src="/img/googleLogin.png"
        /></a>
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
.naver-button {
  margin: 3px;
}
.google-button {
  margin: 1px;
}
</style>
