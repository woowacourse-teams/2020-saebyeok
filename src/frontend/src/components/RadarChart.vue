<script>
import { Radar } from 'vue-chartjs';

export default {
  extends: Radar,
  mounted() {
    this.renderChart({
      labels: ['😄', '😭', '😩', '😍', '😊', '😂'],
      datasets: [
        {
          label: '감정 분포도',
          backgroundColor: '#f87979',
          data: [30, 20, 18, 39, 10, 40]
        }
      ]
    });
  },
  data() {
    return {};
  },
  components: {},
  props: {
    source: String
  }
};
</script>
