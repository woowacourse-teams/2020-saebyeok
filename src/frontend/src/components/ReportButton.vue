<template>
  <div>
    <v-icon @click.stop="onClickAlarmButton()" color="red darken-4"
      >mdi-alarm-light</v-icon
    >
    <v-dialog v-model="dialog" max-width="400">
      <v-card>
        <v-card-title class="text-h7 pl-3">
          이 {{ getReportTypeText() }}을 신고하시겠어요?
        </v-card-title>

        <v-card-actions>
          <v-row>
            <v-col cols="12" align="center" justify="end">
              <v-chip-group
                active-class="deep-purple--text text--accent-4"
                column
                align="center"
                justify="end"
                v-model="choiceCategory"
              >
                <v-chip
                  class="ma-1"
                  style="font-size: 12px; padding: 5px"
                  v-for="reportCategory in this.reportCategories"
                  @click="invalidCategoryChoice = false"
                  :key="reportCategory.id"
                >
                  {{ reportCategory.name }}
                </v-chip>
              </v-chip-group>

              <div style="padding-left: 10px;">
                <h5
                  v-if="this.invalidCategoryChoice"
                  style="color: red; font-weight: lighter"
                  align="left"
                >
                  신고하시는 이유를 선택해 주세요
                </h5>
              </div>
            </v-col>
            <v-col cols="12">
              <v-textarea
                solo
                auto-grow
                hide-details
                rows="1"
                name="input-7-4"
                label="상세한 내용을 적어주세요"
                v-model="textContent"
              />
            </v-col>
            <v-col cols="12" align="right" justify="end">
              <v-btn color="#B2A4D4" text @click="dialog = false">아니요</v-btn>
              <v-btn color="#B2A4D4" text @click="onReport">네, 할게요</v-btn>
            </v-col>
          </v-row>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>
<script>
import { REPORT_TYPE } from '@/utils/ReportType.js';
import { mapMutations } from 'vuex';
import { SHOW_SNACKBAR } from '@/store/shared/mutationTypes';

export default {
  name: 'ReportButton',
  data() {
    return {
      dialog: false,
      reportCategories: [
        { id: 1, name: '광고 목적 게시' },
        { id: 2, name: '모욕적인 표현' },
        { id: 3, name: '불쾌한 표현' }
      ],
      choiceCategory: undefined,
      invalidCategoryChoice: false,
      textContent: ''
    };
  },
  methods: {
    ...mapMutations([SHOW_SNACKBAR]),
    onReport() {
      if (this.choiceCategory === undefined) {
        this.invalidCategoryChoice = true;
        return;
      }
      //todo : 여기에 신고 연산이 들어간다

      if (this.reportType === REPORT_TYPE.ARTICLE) {
        console.log('report this article : ', this.reportedId);
      } else if (this.reportType === REPORT_TYPE.COMMENT) {
        console.log('report this comment : ', this.reportedId);
      }
      this.dialog = false;
      this.showSnackbar('신고가 접수되었습니다. 감사합니다.');
    },
    onClickAlarmButton() {
      this.invalidCategoryChoice = false;
      this.choiceCategory = undefined;
      this.textContent = '';
      this.dialog = true;
    },
    getReportTypeText() {
      if (this.reportType === REPORT_TYPE.ARTICLE) {
        return '게시물';
      }
      return '댓글';
    }
  },
  props: {
    reportType: {
      type: String,
      required: true,
      default: ''
    },
    reportedId: {
      type: Number,
      required: true,
      default: 0
    }
  }
};
</script>
