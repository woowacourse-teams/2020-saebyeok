<template>
  <div class="text-center">
    <v-menu
      v-model="menu"
      :close-on-content-click="false"
      :nudge-width="360"
      offset-y
    >
      <template v-slot:activator="{ on, attrs }">
        <v-icon v-bind="attrs" v-on="on">mdi-filter</v-icon>
      </template>
      <emotion-filter />
    </v-menu>
  </div>
</template>

<script>
import EmotionFilter from '../components/EmotionFilter';

export default {
  components: { EmotionFilter },
  data: () => ({
    menu: false
  })
};
</script>

<style>
.navbarSpace {
  margin-bottom: -10px;
}
</style>
