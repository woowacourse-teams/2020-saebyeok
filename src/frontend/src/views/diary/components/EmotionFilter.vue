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
        <v-chip @click="viewAll()">모두 보기</v-chip>
      </v-col>
      <v-col
        v-for="emotion in emotions"
        :key="emotion.name"
        @click="toggleFeature(emotion)"
        class="col-1"
        :class="{ grayscale: !isSelected(emotion) }"
        style="font-size: 30px; text-align: center; line-height: 40px; padding: 14px 0px 12px 0px;"
        >{{ emotion.icon }}</v-col
      >
    </v-row>
  </v-card>
</template>

<script>
const allFilter = ['기쁨', '슬픔', '괴로움', '사랑', '뿌듯', '웃프다'];
export default {
  data() {
    return {
      emotions: [
        {
          name: '기쁨',
          icon: '😄'
        },
        {
          name: '슬픔',
          icon: '😭'
        },
        {
          name: '괴로움',
          icon: '😩'
        },
        {
          name: '사랑',
          icon: '😍'
        },
        {
          name: '뿌듯',
          icon: '😊'
        },
        {
          name: '웃프다',
          icon: '😂'
        }
      ],
      filter: allFilter.slice()
    };
  },
  methods: {
    toggleFeature(emotion) {
      if (this.isSelected(emotion)) {
        const idx = this.filter.indexOf(emotion.name);
        this.filter.splice(idx, 1);
      } else {
        this.filter.push(emotion.name);
      }
    },
    isSelected(emotion) {
      return this.filter.includes(emotion.name);
    },
    viewAll() {
      this.filter = allFilter.slice();
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
