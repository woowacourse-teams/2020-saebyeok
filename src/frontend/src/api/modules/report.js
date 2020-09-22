import ApiService from '@/api';

const BASE_URL = '/reports';

const ReportService = {
  getCategories() {
    return ApiService.get(`${BASE_URL}/categories`);
  },
  createArticleReport(report) {
    return ApiService.post(`${BASE_URL}/article`, report);
  },
  getArticleReport(reportId) {
    return ApiService.get(`${BASE_URL}/artcile/${reportId}`);
  },
  getArticleReports() {
    return ApiService.get(`${BASE_URL}/article`);
  },
  deleteArticleReport(reportId) {
    return ApiService.delete(`${BASE_URL}/article/${reportId}`);
  },
  createCommentReport(report) {
    return ApiService.post(`${BASE_URL}/comment`, report);
  },
  getCommentReport(reportId) {
    return ApiService.get(`${BASE_URL}/comment/${reportId}`);
  },
  getCommentReports() {
    return ApiService.get(`${BASE_URL}/comment`);
  },
  deleteCommentReport(reportId) {
    return ApiService.delete(`${BASE_URL}/comment/${reportId}`);
  }
};

export default ReportService;
