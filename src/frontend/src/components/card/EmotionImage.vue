<template>
  <v-img
    :src="emotion.imageResource"
    :alt="emotion.name"
    max-height="50"
    max-width="50"
  >
  </v-img>
</template>

<script>
export default {
  name: 'EmotionImage',
  props: {
    emotion: {
      type: Object,
      default: function() {
        return {
          imageResource: '',
          name: ''
        };
      },
      required: true
    }
  }
};
</script>
