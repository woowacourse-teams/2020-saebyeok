<template>
  <v-card
    class="mx-auto rounded-lg"
    color="#faf9f5"
    max-width="400"
    style="margin: 12px"
  >
    <v-row class="justify-center">
      <v-col
        class="col-3"
        style="font-size: 40px; text-align: center; line-height: 40px; padding: 10px 0px 12px 0px; "
      >
        <v-chip @click="selectAll()" :disabled="isSelectedAll()"
          >모두 보기</v-chip
        >
      </v-col>
      <v-col
        v-for="emotion in emotions"
        :key="emotion.id"
        @click="toggleFeature(emotion)"
        class="col-1"
        :class="{ grayscale: !isSelected(emotion) }"
        style="align: center; line-height: 40px; padding: 14px 0px 12px 0px;"
      >
        <v-img
          :src="emotion.imageResource"
          :alt="emotion.name"
          max-height="30"
          max-width="30"
        />
      </v-col>
    </v-row>
  </v-card>
</template>

<script>
import { mapActions, mapGetters } from 'vuex';
import { FETCH_EMOTIONS } from '@/store/shared/actionTypes';
export default {
  name: 'EmotionFilter',
  data() {
    return {
      allFilter: [],
      filter: []
    };
  },
  created() {
    this.fetchEmotions().then(() => {
      for (let index in this.emotions) {
        this.allFilter.push(this.emotions[index].id);
      }
      this.allFilter = this.allFilter.sort();
      this.selectAll();
    });
  },
  computed: {
    ...mapGetters(['emotions'])
  },
  methods: {
    ...mapActions([FETCH_EMOTIONS]),
    toggleFeature(emotion) {
      if (this.isSelected(emotion)) {
        const idx = this.filter.indexOf(emotion.id);
        this.filter.splice(idx, 1);
      } else {
        this.filter.push(emotion.id);
        this.filter = this.filter.sort();
      }
      this.$emit('select', this.filter);
    },
    isSelected(emotion) {
      return this.filter.includes(emotion.id);
    },
    isSelectedAll() {
      return this.filter.length === this.allFilter.length;
    },
    selectAll() {
      this.filter = this.allFilter.slice();
      this.$emit('select', this.filter);
    }
  }
};
</script>

<style scoped>
.grayscale {
  -webkit-filter: grayscale(100%);
  filter: grayscale(100%);
}
</style>
