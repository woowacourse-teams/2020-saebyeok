<template>
  <v-container ma-0 pa-0 justify-start>
    <div class="text-body-2" style="font-size:12px; color:rgba(0,0,0,0.6)">
      {{ createdDateWithFormat }}
    </div>
  </v-container>
</template>

<script>
import Vue from 'vue';
import VueMoment from 'vue-moment';
import moment from 'moment-timezone';

Vue.use(VueMoment, {
  moment
});

export default {
  name: 'CreatedDate',
  props: {
    createdDate: {
      type: String
    }
  },
  computed: {
    createdDateWithFormat: function() {
      const date = this.$moment(this.createdDate);
      const now = this.$moment().tz('Asia/Seoul');
      const gap = (now.valueOf() - date.valueOf()) / 1000;
      if (gap > 86400) {
        return (
          date.format('YYYY') +
          '년 ' +
          date.format('MM') +
          '월 ' +
          date.format('DD') +
          '일'
        );
      } else if (gap > 3600) {
        return Math.floor(gap / 3600) + '시간 전';
      } else if (gap > 60) {
        return Math.floor(gap / 60) + '분 전';
      } else {
        return '방금 전';
      }
    }
  }
};
</script>
