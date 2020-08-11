import ApiService from '@/api';

const BASE_URL = '/emotions';

const EmotionService = {
  get(emotionId) {
    return ApiService.get(`${BASE_URL}/${emotionId}`);
  },
  getAll() {
    return ApiService.get(`${BASE_URL}`);
  }
};

export default EmotionService;
