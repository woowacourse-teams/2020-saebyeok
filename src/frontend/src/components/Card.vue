<template>
  <v-card class="mx-auto" color="#faf9f5" max-width="400">
    <v-card-title>
      <v-icon large left>mdi-twitter</v-icon>
      <span class="title font-weight-light">기뻐요</span>
    </v-card-title>

    <v-card-text class="headline">
      "Turns out semicolon-less style is easier and safer in TS because most
      gotcha edge cases are type invalid as well."
    </v-card-text>

    <v-card-actions>
      <v-list-item class="grow">
        <v-row align="center" justify="end">
          <v-icon class="mr-1">mdi-hand-heart</v-icon>
          <span class="subheading mr-2">256</span>
          <v-icon class="mr-1">mdi-alarm-light</v-icon>
          <span class="subheading">45</span>
        </v-row>
      </v-list-item>
    </v-card-actions>
  </v-card>
</template>

<script>
export default {};
</script>
