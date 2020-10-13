import ApiService from '@/api';

const BASE_URL = '/reports';

const ReportService = {
  getCategories() {
    return ApiService.get(`${BASE_URL}/categories`);
  },
  createReport(report) {
    return ApiService.post(`${BASE_URL}`, report);
  }
};

export default ReportService;
