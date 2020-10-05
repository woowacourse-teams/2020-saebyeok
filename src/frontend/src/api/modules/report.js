import ApiService from '@/api';

const BASE_URL = '/reports';

const ReportService = {
  getCategories() {
    return ApiService.get(`${BASE_URL}/categories`);
  },
  createArticleReport(report) {
    return ApiService.post(`${BASE_URL}/article`, report);
  },
  createCommentReport(report) {
    return ApiService.post(`${BASE_URL}/comment`, report);
  }
};

export default ReportService;
