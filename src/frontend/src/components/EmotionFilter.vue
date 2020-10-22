<template>
  <v-card class="mx-auto rounded-lg" max-width="400">
    <v-row class="justify-center align-center">
      <v-col class="col-3 choice-all-text">
        <v-chip @click="selectAll()">
          {{ selectAllButtonText() }}
        </v-chip>
      </v-col>
      <v-col
        v-for="emotion in emotions"
        :key="emotion.id"
        @click="toggleFeature(emotion)"
        class="col-1 chip-text"
        :class="{ grayscale: !isSelectedEmotion(emotion) }"
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
import { FETCH_EMOTIONS, SELECT_FILTER } from '@/store/shared/actionTypes';
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
      this.filter = this.allFilter.slice();
      this.applyFilter();
    });
  },
  computed: {
    ...mapGetters(['emotions']),
    selectAllButtonText() {
      return this.isSelectedAll() ? '전체 선택' : '전체 해제';
    }
  },
  methods: {
    ...mapActions([FETCH_EMOTIONS]),
    ...mapActions([SELECT_FILTER]),
    toggleFeature(emotion) {
      if (this.isSelectedEmotion(emotion)) {
        const idx = this.filter.indexOf(emotion.id);
        this.filter.splice(idx, 1);
      } else {
        this.filter.push(emotion.id);
        this.filter = this.filter.sort();
      }
      this.applyFilter();
    },
    selectAll() {
      if (this.isSelectedAll()) {
        this.filter = [];
      } else {
        this.filter = this.allFilter.slice();
      }
      this.applyFilter();
    },
    isSelectedEmotion(emotion) {
      return this.filter.includes(emotion.id);
    },
    isSelectedAll() {
      return this.filter.length === this.allFilter.length;
    },
    applyFilter() {
      this.selectFilter({
        emotionIds: this.filter.toString(),
        isFiltered: !this.isSelectedAll()
      });
    }
  }
};
</script>

<style scoped>
.grayscale {
  -webkit-filter: grayscale(100%);
  filter: grayscale(100%);
}
.choice-all-text {
  font-size: 40px;
  text-align: center;
  line-height: 40px;
  padding: 10px 0px 12px 0px;
}
.chip-text {
  line-height: 40px;
  padding: 14px 0px 12px 0px;
}
</style>
