import ApiService from '@/api';

const BASE_URL = '/report';

const ReportService = {
  getCategories() {
    return ApiService.get(`${BASE_URL}/category`);
  }
};

export default ReportService;
