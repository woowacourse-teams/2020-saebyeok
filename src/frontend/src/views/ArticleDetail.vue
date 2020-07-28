<template>
  <div>
    {{ $route.params.articleId }}
  </div>
</template>

<script>
export default {
  name: 'ArticleDetail',
  props: ['articleId']
};
</script>
