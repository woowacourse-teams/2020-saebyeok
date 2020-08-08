<template>
  <v-container>
    <v-layout v-on:click="onClickEmotionButton" justify-center align-center>
      <!-- <div class="text-h2">😊</div> -->
      <v-img
        :src="emotion.imageResource"
        :alt="emotion.name"
        max-height="100"
        max-width="100"
      >
      </v-img>
    </v-layout>
  </v-container>
</template>

<script>
export default {
  name: 'EmotionButton',
  props: {
    emotion: {
      type: Object,
      required: true
    }
  },
  methods: {
    onClickEmotionButton: function() {
      this.$router.push({
        path: 'post/' + this.emotion.id
      });
    }
  }
};
</script>
