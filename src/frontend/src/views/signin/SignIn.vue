<template>
  <v-app>
    <v-layout column align-center fill-height class="background">
      <v-flex class="ma-16 pt-16">
        <p id="title">새벽</p>
        <p id="description">함께 쓰는 감정 비밀 다이어리</p>
      </v-flex>
      <v-flex style="text-align:center">
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
        <a class="mt-4 linkToFeed" href="/feed">
          로그인 없이 둘러보기
        </a>
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
.naver-button {
  margin: 3px;
}
.google-button {
  margin: 1px;
}
.linkToFeed {
  color: grey;
  display: block;
}
</style>
