<template>
  <v-container pa-0 ma-0>
    <v-img
      :src="emotion.imageResource"
      :alt="emotion.name"
      max-height="80"
      max-width="80"
    >
    </v-img>
  </v-container>
</template>

<script>
export default {
  name: 'EmotionImage',
  props: {
    emotion: {
      type: Object,
      required: true
    }
  }
};
</script>
