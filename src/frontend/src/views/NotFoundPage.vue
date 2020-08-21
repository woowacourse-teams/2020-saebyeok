<template>
  <v-app id="inspire">
    <Header />
    <v-main>
      <v-card>asdf</v-card>
    </v-main>
    <Footer />
  </v-app>
</template>

<script>
import Header from '../components/core/Header';
import Footer from '../components/core/Footer';

export default {
  name: 'Index',

  components: {
    Header,
    Footer
  }
};
</script>

<style>
#inspire {
  background-color: rgba(227, 214, 244, 0.5);
}
</style>
