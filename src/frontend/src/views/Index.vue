<template>
  <p>로딩 중..</p>
</template>

<script>
export default {
  mounted() {
    window.location.href = '/signin';
    // this.$router.push('/signin');
  }
};
</script>

<style></style>
