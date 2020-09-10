<template>
  <v-app>
    <v-layout column align-center fill-height class="background">
      <v-flex class="ma-16 pt-16">
        <p id="title">새벽</p>
        <p id="description">감정 공유 비밀 다이어리</p>
      </v-flex>
      <v-flex>
        <a href="/oauth2/authorization/naver">
          <v-img
            alt="네이버 아이디로 로그인"
            width="200px"
            src="/img/naverLogin.png"
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
#title {
  font-size: 40px;
  color: white;
  text-align: center;
}
#description {
  text-align: center;
}
</style>
