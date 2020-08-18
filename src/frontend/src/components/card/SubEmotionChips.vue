<template>
  <v-container pa-0 ma-0>
    <v-row no-gutters>
      <v-chip
        v-for="subEmotion in subEmotions"
        :key="subEmotion.id"
        class="ma-1"
        pa-2
      >
        # {{ subEmotion.name }}
      </v-chip>
    </v-row>
  </v-container>
</template>

<script>
export default {
  name: 'SubEmotionChips',
  props: {
    subEmotions: {
      type: Array,
      default: function() {
        return [];
      },
      required: true
    }
  }
};
</script>
