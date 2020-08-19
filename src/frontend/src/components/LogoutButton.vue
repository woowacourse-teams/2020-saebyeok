<template>
  <div>
    <v-icon large @click.stop="dialog = true">mdi-logout</v-icon>
    <v-dialog v-model="dialog" max-width="290">
      <v-card>
        <v-card-title class="headline">정말 로그아웃 하시겠어요?</v-card-title>

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
export default {
  data() {
    return {
      dialog: false
    };
  },
  methods: {
    logout() {
      localStorage.clear();
      location.href = '/signin';
    }
  }
};
</script>

<style></style>
