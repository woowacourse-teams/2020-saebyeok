<template>
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
</template>

<script>
export default {};
</script>

<style>
.background {
  /* Todo: 우선 배경화면 이미지를 인터넷에서 가져옴. 추후 이미지 파일로 관리 필요 */
  background-image: url('https://www.starobserver.org/image/1711/20171117dawnTezel.jpg');
  background-position: center center;
}
p {
  font-size: 40px;
  color: white;
}
</style>
