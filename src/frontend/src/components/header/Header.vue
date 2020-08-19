<template>
  <v-app-bar app clipped-right color="#B2A4D4" flat dark>
    <filter-button />
    <v-spacer></v-spacer>
    <v-toolbar-title>
      <span class="title text-h4">새벽</span>
    </v-toolbar-title>
    <v-spacer></v-spacer>
    <v-app-bar-nav-icon>
      <v-icon large @click.stop="dialog = true">mdi-logout</v-icon>
      <v-dialog v-model="dialog" max-width="290">
        <v-card>
          <v-card-title class="headline"
            >정말 로그아웃 하시겠어요?</v-card-title
          >

          <v-card-actions>
            <v-spacer></v-spacer>

            <v-btn color="#B2A4D4" text @click="dialog = false">아니요</v-btn>

            <v-btn color="#B2A4D4" text @click="logout">네, 할게요</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-app-bar-nav-icon>
  </v-app-bar>
</template>

<script>
import FilterButton from '@/components/FilterButton';

export default {
  name: 'Header',
  components: {
    FilterButton
  },
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
