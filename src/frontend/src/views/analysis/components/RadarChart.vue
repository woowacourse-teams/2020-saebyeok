<script>
    import {Radar} from 'vue-chartjs';

    export default {
        extends: Radar,
        mounted() {
            this.renderChart(this.data, this.options);
        },
        data() {
            return {
                data: {
                    labels: ['😄', '😭', '😩', '😍', '😊', '😂'],
                    datasets: [
                        {
            label: '감정 분포도',
            borderColor: '#B2A4D4',
            data: [30, 20, 18, 39, 10, 40]
          }
        ]
      },
      options: {
        scale: {
          pointLabels: {
            fontSize: 25
          }
        }
      }
    };
  },
  components: {},
  props: {
    source: String
  }
};
</script>
