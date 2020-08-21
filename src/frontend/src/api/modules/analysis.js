import ApiService from '@/api';

const BASE_URL = '/analysis';

const AnalysisService = {
  getArticlesAnalysis() {
    return ApiService.get(`${BASE_URL}/articles`);
  },
  getCommentsAnalysis() {
    return ApiService.get(`${BASE_URL}/comments`);
  }
};

export default AnalysisService;
