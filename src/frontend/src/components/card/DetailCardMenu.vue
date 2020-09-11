<template>
  <v-menu bottom left>
    <template v-slot:activator="{ on, attrs }">
      <v-btn icon v-bind="attrs" v-on="on">
        <v-icon>mdi-dots-vertical</v-icon>
      </v-btn>
    </template>

    <v-list>
      <v-list-item @click.stop="dialog = true">
        <v-list-item-title>삭제하기</v-list-item-title>
        <v-dialog v-model="dialog" max-width="320">
          <v-card>
            <v-card-title class="text-h6"
              >정말 게시물을 삭제하시겠어요?</v-card-title
            >

            <v-card-actions>
              <v-spacer></v-spacer>

              <v-btn color="#B2A4D4" text @click="dialog = false">아니요</v-btn>

              <v-btn color="#B2A4D4" text @click="onDeleteArticle"
                >네, 할게요</v-btn
              >
            </v-card-actions>
          </v-card>
        </v-dialog>
      </v-list-item>
    </v-list>
  </v-menu>
</template>
<script>
import { mapActions } from 'vuex';
import { DELETE_ARTICLE } from '@/store/shared/actionTypes';

export default {
  name: 'DetailCardMenu',
  data() {
    return {
      dialog: false
    };
  },
  methods: {
    ...mapActions([DELETE_ARTICLE]),
    onDeleteArticle() {
      this.deleteArticle(this.articleId).then(() => {
        if (this.$route.params.articleId === undefined) {
          this.$router.go();
        } else {
          this.$router.go(-1);
        }
      });
    }
  },
  props: {
    articleId: {
      type: Number,
      required: true
    }
  }
};
</script>
