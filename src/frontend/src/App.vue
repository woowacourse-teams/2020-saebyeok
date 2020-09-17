<template>
  <div>
    <router-view />
    <Snackbar />
    <Login />
  </div>
</template>

<script>
import Snackbar from './components/Snackbar';
import Login from '@/components/Login.vue';
export default {
  name: 'App',
  components: {
    Snackbar,
    Login
  }
};
</script>

<style scoped></style>
