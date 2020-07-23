<template>
  <div>
    <h1>글쓰기 화면</h1>
    <editor></editor>
  </div>
</template>

<script>
import Editor from '@/components/Editor.vue';

export default {
  components: {
    Editor
  },
  props: {
    source: String
  }
};
</script>
