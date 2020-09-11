<template>
  <div>
    <v-bottom-navigation
      app
      color="#B2A4D4"
      shift
      :class="{ navbarSpace: navbarSpaceNeeded }"
    >
      <v-btn to="/feed" height="100%">
        <span>피드</span>
        <v-icon large>mdi-rss</v-icon>
      </v-btn>

      <v-btn to="/emotions" height="100%">
        <span>글쓰기</span>
        <v-icon large>mdi-pencil</v-icon>
      </v-btn>

      <v-btn to="/my-page" height="100%">
        <span>다이어리</span>
        <v-icon large>mdi-note</v-icon>
      </v-btn>
    </v-bottom-navigation>
  </div>
</template>

<script>
import { mapActions } from 'vuex';
import { CHECK_NAVBAR_SPACE_NEEDED } from '@/store/shared/actionTypes';
export default {
  name: 'bottom-navigation',
  mounted() {
    this.checkNavbarSpaceNeeded();
  },
  computed: {
    navbarSpaceNeeded() {
      return this.$store.getters.spaceNeeded;
    }
  },
  methods: {
    ...mapActions([CHECK_NAVBAR_SPACE_NEEDED])
  }
};
</script>

<style>
.navbarSpace {
  height: 76px !important;
  padding-bottom: 20px;
}
</style>
