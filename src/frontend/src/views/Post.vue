<template>
  <div>
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
