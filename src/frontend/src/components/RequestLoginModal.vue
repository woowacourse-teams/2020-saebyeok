<template>
  <v-container justify="center">
    <v-dialog max-width="290" persistent v-model="isShowRequestLoginModal">
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
          <v-btn @click="goToFeed" color="grey darken-1" text>
            돌아가기
          </v-btn>
          <v-btn @click="goToSignIn" color="deep-purple darken-1" text>
            로그인
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
import { mapGetters, mapMutations } from 'vuex';
import { HIDE_REQUEST_LOGIN_MODAL } from '@/store/shared/mutationTypes';
export default {
  name: 'Login',
  computed: {
    ...mapGetters(['isShowRequestLoginModal'])
  },
  methods: {
    ...mapMutations([HIDE_REQUEST_LOGIN_MODAL]),
    goToFeed() {
      this.hideRequestLoginModal();
      this.$router.push('/feed');
    },
    goToSignIn() {
      this.hideRequestLoginModal();
      this.$router.push('/signin');
    }
  }
};
</script>

<style scoped></style>
