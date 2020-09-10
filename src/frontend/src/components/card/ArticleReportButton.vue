<template>
  <div>
    <v-icon @click.stop="dialog = true">mdi-alarm-light</v-icon>
    <v-dialog v-model="dialog" max-width="400">
      <v-card>
        <v-card-title class="text-h7">
          이 게시물을 신고하시겠어요?
        </v-card-title>

        <v-card-actions>
          <v-row>
            <v-col cols="12" align="center" justify="end">
              <v-chip-group
                active-class="deep-purple--text text--accent-4"
                column
                align="center"
                justify="end"
              >
                <v-chip
                  class="ma-1"
                  style="font-size: 12px; padding: 5px"
                  v-for="reportCategory in this.reportCategories"
                  v-on:click="onClickReportCategory(reportCategory.id)"
                  :key="reportCategory.id"
                >
                  {{ reportCategory.name }}
                </v-chip>
              </v-chip-group>

              <h5
                v-if="this.invalidCategoryChoice"
                style="color: red; font-weight: lighter"
              >
                신고하시는 이유를 선택해 주세요
              </h5>
            </v-col>
            <v-col cols="12">
              <v-textarea
                solo
                auto-grow
                hide-details
                rows="1"
                name="input-7-4"
                label="상세한 내용을 적어주시면 확인하겠습니다"
                v-model="content"
              />
            </v-col>
            <v-col cols="12" align="right" justify="end">
              <v-btn color="#B2A4D4" text @click="dialog = false">아니요</v-btn>
              <v-btn
                color="#B2A4D4"
                text
                @click="onReportArticle"
                :disabled="this.choiceCaregoryId === 0"
                >네, 할게요</v-btn
              >
            </v-col>
          </v-row>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>
<script>
export default {
  name: 'ArticleReportButton',
  data() {
    return {
      dialog: false,
      reportCategories: [
        { id: 1, name: '광고 목적 게시' },
        { id: 2, name: '모욕적인 표현' },
        { id: 3, name: '불쾌한 표현' }
      ],
      choiceCategoryId: 0,
      invalidCategoryChoice: false
    };
  },
  methods: {
    onReportArticle() {
      if (this.choiceCategoryId === 0) {
        this.invalidCategoryChoice = true;
        return;
      }
      console.log('report complete');
    },
    onClickReportCategory(reportCategoryId) {
      this.invalidCategoryChoice = false;
      if (this.choiceCategoryId === reportCategoryId) {
        this.choiceCategoryId = 0;
        return;
      }
      this.choiceCategoryId = reportCategoryId;
    }
  }
};
</script>
