<template>
  <v-row justify="center">
    <v-dialog v-model="dialog" persistent max-width="400">
      <v-card>
        <v-carousel :continuous="false" hide-delimiters height="auto">
          <v-carousel-item
            v-for="(item, i) in items"
            :key="i"
            reverse-transition="fade-transition"
            transition="fade-transition"
            eager
            ><v-img :src="item.src" height="100%" eager />
          </v-carousel-item>
        </v-carousel>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="purple darken-5" text @click="closeTutorial()"
            >시작해볼게요</v-btn
          >
          <v-spacer></v-spacer>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-row>
</template>

<script>
export default {
  props: ['dialog'],
  data() {
    return {
      items: [
        {
          src: '/img/tutorial_1.png'
        },
        {
          src: '/img/tutorial_2.png'
        },
        {
          src: '/img/tutorial_3.png'
        },
        {
          src: '/img/tutorial_4.png'
        },
        {
          src: '/img/tutorial_5.png'
        },
        {
          src: '/img/tutorial_6.png'
        },
        {
          src: '/img/tutorial_7.png'
        }
      ]
    };
  },
  methods: {
    closeTutorial() {
      this.$emit('closeTutorial');
      localStorage.setItem('tutorial_check', 'yes');
    }
  }
};
</script>
