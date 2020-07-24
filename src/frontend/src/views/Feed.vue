<template>
  <div>
    <div class="mt-4 overflow-y-auto">
      <v-list-item-group color="grey darken-3" v-model="articles">
        <v-list-item :key="article.content" v-for="article in articles">
          <v-list-item-content>
            <!--            <v-list-item-title @click="setLineDetail(line)">-->
            <!--              -->
            <!--              <span>{{ line.name }}</span>-->
            <!--            </v-list-item-title>-->
            <Card></Card>
            {{ article.content }}
          </v-list-item-content>
        </v-list-item>
      </v-list-item-group>
    </div>
  </div>
</template>

<script>
  import Card from '@/components/Card.vue';
  import FeedService from '@/api/modules/feed.js';

  export default {
    data() {
      return {articles: []};
    },
    components: {
      Card
    },
    created() {
      this.getAll()
              .then(({data}) => {
                this.articles = data;

                console.log(this.articles);
                console.log(data);
              })
              .catch(() => {
                alert('에러가 발생했습니다.');
              });
    },
    methods: {
      async getAll() {
        return FeedService.getAll();
      }
    },
    props: {
      source: String
    }
  };
</script>
