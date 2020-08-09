<template>
  <v-container pa-0 ma-0>
    <v-chip-group
      active-class="black--text text--accent-4"
      column
      multiple
      align="right"
      justify="end"
    >
      <v-chip v-for="subEmotion in subEmotions" :key="subEmotion.id">
        # {{ subEmotion.name }}
      </v-chip>
    </v-chip-group>
  </v-container>
</template>

<script>
export default {
  name: 'SubEmotionTab',
  props: {
    subEmotions: {
      type: Array,
      required: true
    }
  }
};
</script>
