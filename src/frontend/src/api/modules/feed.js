import ApiService from '@/api';

const BASE_URL = '/articles';

const FeedService = {
  getAll() {
    return ApiService.get(`${BASE_URL}`);
  }
};

export default FeedService;
