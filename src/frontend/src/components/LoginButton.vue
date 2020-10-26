<template>
  <div>
    <v-icon @click.stop="dialog = true">mdi-login</v-icon>
    <v-dialog v-model="dialog" max-width="290">
      <v-card>
        <v-card-title class="text-h6">새벽에 로그인 하시겠어요?</v-card-title>

        <v-card-actions>
          <v-spacer></v-spacer>

          <v-btn color="#B2A4D4" text @click="dialog = false">아니요</v-btn>

          <v-btn color="#B2A4D4" text @click="login">네, 할게요</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import { mapMutations } from 'vuex';
import { SHOW_SNACKBAR } from '../store/shared/mutationTypes';

export default {
  name: 'LoginButton',
  data() {
    return {
      dialog: false
    };
  },
  methods: {
    ...mapMutations([SHOW_SNACKBAR]),
    login() {
      this.$router.push('/signin');
    }
  }
};
</script>

<style></style>
