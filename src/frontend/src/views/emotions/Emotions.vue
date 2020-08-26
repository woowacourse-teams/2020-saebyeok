<template>
  <div>
    <v-container>
      <div class="mt-8 text-h4">
        지금 당신의
        <p>기분은 어떤가요?</p>
      </div>
      <v-flex>
        <v-row dense>
          <v-col cols="4">
            <emotion-button :emotion="emotions[0]" />
          </v-col>
          <v-col cols="4">
            <emotion-button :emotion="emotions[1]" />
          </v-col>
          <v-col cols="4">
            <emotion-button :emotion="emotions[2]" />
          </v-col>
          <v-col cols="2" />
          <v-col cols="4">
            <emotion-button :emotion="emotions[3]" />
          </v-col>
          <v-col cols="4">
            <emotion-button :emotion="emotions[4]" />
          </v-col>
          <v-col cols="2" />
        </v-row>
      </v-flex>
    </v-container>
  </div>
</template>

<script>
import { mapActions, mapGetters } from 'vuex';
import { FETCH_EMOTIONS } from '@/store/shared/actionTypes';
import EmotionButton from './components/EmotionButton';
export default {
  name: 'Emotions',
  components: {
    EmotionButton
  },
  created() {
    this.fetchEmotions();
  },
  computed: {
    ...mapGetters(['emotions'])
  },
  methods: {
    ...mapActions([FETCH_EMOTIONS])
  }
};
</script>

<style scoped></style>
