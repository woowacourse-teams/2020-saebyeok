<template>
  <v-menu bottom left>
    <template v-slot:activator="{ on, attrs }">
      <v-btn icon v-bind="attrs" v-on="on">
        <v-icon>mdi-dots-vertical</v-icon>
      </v-btn>
    </template>

    <v-list>
      <v-list-item @click="onDeleteButton">
        <v-list-item-title>삭제하기</v-list-item-title>
      </v-list-item>
    </v-list>
  </v-menu>
</template>
<script>
export default {
  name: 'DetailCardMenu',
  methods: {
    onDeleteButton() {
      console.log('delete it!');
    }
  }
};
</script>
