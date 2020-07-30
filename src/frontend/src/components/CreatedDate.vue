<template>
  <v-container ma-0 pa-0 class="d-flex justify-end">
    <div class="text-body-2">
      {{ createdDateWithFormat }}
    </div>
  </v-container>
</template>

<script>
export default {
  name: 'CreatedDate',
  props: {
    createdDate: {
      type: String
    }
  },
  computed: {
    createdDateWithFormat: function() {
      const date = new Date(this.createdDate);
      const now = new Date();
      const gap = (now.getTime() - date.getTime()) / 1000;
      if (gap > 86400) {
        return (
          date.getFullYear() +
          '년 ' +
          (date.getMonth() + 1) +
          '월 ' +
          date.getDate() +
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
