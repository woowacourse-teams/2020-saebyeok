<template>
  <v-snackbar :timeout="6000" multi-line v-model="snackbar">
    <div class="text-center width-100 font-size-15">{{ snackbarMsg }}</div>
  </v-snackbar>
</template>

<script>
import { mapGetters } from 'vuex';
import { mapMutations } from 'vuex';
import { HIDE_SNACKBAR } from '@/store/shared/mutationTypes';

export default {
  name: 'Snackbar',
  computed: {
    ...mapGetters(['isShow', 'message'])
  },
  watch: {
    isShow() {
      if (this.isShow === true) {
        this.showSnackbar(this.message);
      }
    },
    snackbar() {
      if (this.snackbar === false) {
        this.hideSnackbar();
      }
    }
  },
  methods: {
    ...mapMutations([HIDE_SNACKBAR]),
    showSnackbar(msg) {
      this.snackbarMsg = msg;
      this.snackbar = true;
    }
  },
  data() {
    return {
      snackbar: false,
      snackbarMsg: ''
    };
  }
};
</script>
