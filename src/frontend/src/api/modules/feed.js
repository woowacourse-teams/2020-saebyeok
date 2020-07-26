import ApiService from '@/api';

const BASE_URL = '/articles';

const FeedService = {
  get(articleId) {
    return ApiService.get(`${BASE_URL}/${articleId}`);
  },
  getAll() {
    return ApiService.get(`${BASE_URL}`);
  }
};

export default FeedService;
