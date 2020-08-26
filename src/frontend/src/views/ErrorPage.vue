<template>
  <v-app id="inspire">
    <Header />
    <v-main>
      <v-spacer />
      <v-row align="center" justify="center" style="height:100%">
        <v-col>
          <v-card class="mx-auto rounded-lg" max-width="400">
            <v-card-title>
              <big>Error</big>
            </v-card-title>
            <v-card-text
              >에러가 발생했습니다. 빠른 시간 내에 해결할게요.</v-card-text
            >
            <v-layout align-center justify-center>
              <v-btn
                class="ma-2"
                align-center
                justify-center
                depressed
                dark
                color="rgba(164, 63, 176)"
                width="95%"
                style="padding:0px"
                @click="backToFeed"
                >피드로 돌아가기</v-btn
              >
            </v-layout>
          </v-card>
        </v-col>
      </v-row>
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
  },
  methods: {
    backToFeed() {
      location.href = '/feed';
    }
  }
};
</script>

<style>
#inspire {
  background-color: rgb(245, 245, 245) !important;
}
</style>
