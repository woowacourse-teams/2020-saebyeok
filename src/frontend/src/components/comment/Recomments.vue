<template>
  <v-container pa-0>
    <v-card class="mx-auto" max-width="400" flat>
      <v-row dense>
        <v-col
          v-for="recomment in recomments"
          :key="recomment.id"
          cols="12"
          class="justify-center pt-0 pb-0"
        >
          <recomment :recomment="recomment"></recomment>
        </v-col>
      </v-row>
    </v-card>
  </v-container>
</template>
<script>
import Recomment from '@/components/comment/Recomment';
export default {
  name: 'Recomments',
  components: {
    Recomment
  },
  data() {
    return {};
  },
  props: {
    recomments: {
      type: Array,
      required: true
    }
  }
};
</script>
