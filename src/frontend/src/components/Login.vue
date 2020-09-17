<template>
  <v-row justify="center">
    <v-dialog max-width="290" persistent v-model="dialog">
      <v-card>
        <v-card-title class="headline">
          반가워요👋
        </v-card-title>
        <v-card-text>
          새벽이 마음에 들었나요?<br />
          소셜 로그인으로 간단하게 회원가입하고 모든 기능을 사용해보세요!
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn @click="dialog = false" color="grey darken-1" text>
            돌아가기
          </v-btn>
          <v-btn @click="dialog = false" color="deep-purple darken-1" text>
            로그인
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-row>
</template>

<script>
export default {
  props: ['dialog']
};
</script>

<style scoped></style>
