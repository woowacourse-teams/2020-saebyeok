<template>
  <div>
    <v-icon
      @click.stop="onClickAlarmButton()"
      color="red darken-4"
      style="font-size:20px;"
      >mdi-alarm-light-outline</v-icon
    >
    <v-dialog v-model="dialog" max-width="400">
      <v-card>
        <v-card-title class="text-h7 pl-3">
          이 {{ getReportTargetText() }}을 신고하시겠어요?
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
                  class="ma-1 report-category-text"
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
                  class="alertCaption"
                  align="left"
                >
                  신고 분류를 선택해 주세요
                </h5>
                <h5
                  v-if="this.choiceCategory !== undefined"
                  class="caption"
                  align="left"
                >
                  {{ reportCategories[this.choiceCategory].content }}
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
import { REPORT_TARGET } from '@/utils/ReportTarget.js';
import {
  CREATE_REPORT,
  FETCH_REPORT_CATEGORIES
} from '@/store/shared/actionTypes';
import {
  SHOW_SNACKBAR,
  SHOW_REQUEST_LOGIN_MODAL
} from '@/store/shared/mutationTypes';

import { mapActions, mapGetters, mapMutations } from 'vuex';

export default {
  name: 'ReportButton',
  data() {
    return {
      dialog: false,
      choiceCategory: undefined,
      invalidCategoryChoice: false,
      textContent: '',
      token: localStorage.getItem('token')
    };
  },
  created() {
    this.fetchReportCategories();
  },
  computed: {
    ...mapGetters(['reportCategories']),
    ...mapGetters(['reportTarget'])
  },
  methods: {
    ...mapMutations([SHOW_SNACKBAR]),
    ...mapMutations([SHOW_REQUEST_LOGIN_MODAL]),
    ...mapActions([FETCH_REPORT_CATEGORIES]),
    ...mapActions([CREATE_REPORT]),
    onReport() {
      if (this.choiceCategory === undefined) {
        this.invalidCategoryChoice = true;
        return;
      }
      this.submitReport();
    },
    submitReport() {
      const reportCreateRequest = {
        content: this.textContent,
        targetContentId: this.reportTarget.contentId,
        reportTarget: this.reportTarget.target,
        reportCategoryId: this.reportCategories[this.choiceCategory].id
      };
      this.createReport(reportCreateRequest)
        .then(() => {
          this.dialog = false;
          this.showSnackbar('신고가 접수되었습니다. 감사합니다.');
        })
        .catch(error => {
          this.showSnackbar(error.response.data.errorMessage);
        });
    },
    onClickAlarmButton() {
      this.invalidCategoryChoice = false;
      this.choiceCategory = undefined;
      this.textContent = '';
      this.dialog = true;
      this.checkLoginUser();
      this.$emit('click');
    },
    checkLoginUser() {
      if (this.token === null) {
        this.dialog = false;
        this.showRequestLoginModal();
      }
    },
    getReportTargetText() {
      switch (this.reportTarget.target) {
        case REPORT_TARGET.ARTICLE:
          return '게시물';
        case REPORT_TARGET.COMMENT:
          return '댓글';
        default:
          return '게시물';
      }
    }
  }
};
</script>
<style scoped>
.alertCaption {
  color: red;
  font-weight: lighter;
}
.caption {
  font-weight: lighter;
}
.report-category-text {
  font-size: 12px;
  padding: 5px;
}
</style>
