import ApiService from '@/api';

const BASE_URL = '/reports';

const ReportService = {
  getCategories() {
    return ApiService.get(`/reports/categories`);
  },
  create(report) {
    return ApiService.post(`${BASE_URL}`, report);
  },
  get(reportId) {
    return ApiService.get(`${BASE_URL}/${reportId}`);
  },
  getAll(params) {
    return ApiService.getWithParams(`${BASE_URL}`, params);
  },
  delete(reportId) {
    return ApiService.delete(`${BASE_URL}/${reportId}`);
  }
};

export default ReportService;
