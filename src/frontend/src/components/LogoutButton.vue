<template>
  <div>
    <v-icon @click.stop="dialog = true">mdi-logout</v-icon>
    <v-dialog v-model="dialog" max-width="290">
      <v-card>
        <v-card-title class="text-h6">정말 로그아웃 하시겠어요?</v-card-title>

        <v-card-actions>
          <v-spacer></v-spacer>

          <v-btn color="#B2A4D4" text @click="dialog = false">아니요</v-btn>

          <v-btn color="#B2A4D4" text @click="logout">네, 할게요</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import { mapMutations } from 'vuex';
import { SHOW_SNACKBAR } from '../store/shared/mutationTypes';

export default {
  data() {
    return {
      dialog: false
    };
  },
  methods: {
    ...mapMutations([SHOW_SNACKBAR]),
    logout() {
      localStorage.clear();
      this.showSnackbar('성공적으로 로그아웃되었어요. 안녕히 가세요👋');
      this.$router.replace({ name: 'SignIn' });
    }
  }
};
</script>

<style></style>
