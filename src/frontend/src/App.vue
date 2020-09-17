<template>
  <div>
    <router-view />
    <Snackbar />
    <RequestLoginModal />
  </div>
</template>

<script>
import Snackbar from './components/Snackbar';
import RequestLoginModal from '@/components/RequestLoginModal.vue';
export default {
  name: 'App',
  components: {
    Snackbar,
    RequestLoginModal
  }
};
</script>

<style scoped></style>
