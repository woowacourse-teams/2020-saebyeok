<template>
  <v-app id="inspire">
    <Header />
    <v-main>
      <v-container fluid>
        <Main />
      </v-container>
    </v-main>
    <Footer />
  </v-app>
</template>

<script>
import Header from '../components/core/Header';
import Main from '../components/core/Main';
import Footer from '../components/core/Footer';

export default {
  name: 'Index',

  components: {
    Header,
    Main,
    Footer
  }
};
</script>

<style>
#inspire {
  background-color: rgb(245, 245, 245) !important;
}
</style>
