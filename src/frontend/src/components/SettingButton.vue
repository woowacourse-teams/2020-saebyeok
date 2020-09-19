<template>
  <v-menu bottom left>
    <template v-slot:activator="{ on, attrs }">
      <v-btn icon v-bind="attrs" v-on="on">
        <v-icon>mdi-account-settings</v-icon>
      </v-btn>
    </template>

    <v-list>
      <v-list-item @click.stop="logoutDialog = true">
        <v-list-item-title>로그아웃</v-list-item-title>
        <v-dialog v-model="logoutDialog" max-width="290">
          <v-card>
            <v-card-title class="text-h6"
              >정말 로그아웃 하시겠어요?</v-card-title
            >
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="#B2A4D4" text @click="logoutDialog = false"
                >아니요</v-btn
              >
              <v-btn color="#B2A4D4" text @click="logout">네, 할게요</v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </v-list-item>
      <v-list-item @click.stop="dialog = true">
        <v-list-item-title>탈퇴</v-list-item-title>
        <v-dialog v-model="dialog" max-width="290">
          <v-card>
            <v-card-title class="text-h6">정말 떠나시는 거예요?😭</v-card-title>
            <v-card-text>
              * 탈퇴 시 작성하신 게시글은 자동으로 삭제되지 않습니다. <br />
              * 개인정보(소셜 로그인 식별 번호)는 삭제되며, 추후 같은 소셜
              로그인 아이디로 다시 가입하시더라도 작성하셨던 게시글을 열람이나
              삭제할 수 없습니다.
            </v-card-text>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="#B2A4D4" text @click="dialog = false">아니요</v-btn>
              <v-btn color="red darken-4" text @click="unregister"
                >탈퇴하기</v-btn
              >
            </v-card-actions>
          </v-card>
        </v-dialog>
      </v-list-item>
    </v-list>
  </v-menu>
</template>

<script>
import { mapMutations } from 'vuex';
import { SHOW_SNACKBAR } from '../store/shared/mutationTypes';

export default {
  data() {
    return {
      dialog: false,
      logoutDialog: false
    };
  },
  methods: {
    ...mapMutations([SHOW_SNACKBAR]),
    logout() {
      localStorage.clear();
      this.showSnackbar('성공적으로 로그아웃되었어요. 안녕히 가세요👋');
      this.$router.replace({ name: 'SignIn' });
    },
    unregister() {
      localStorage.clear();

      this.showSnackbar('성공적으로 탈퇴되었어요. 안녕히 가세요👋');
      this.$router.replace({ name: 'SignIn' });
    }
  }
};
</script>

<style></style>
