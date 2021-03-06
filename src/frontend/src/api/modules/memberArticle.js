import ApiService from '@/api';

const BASE_URL = '/member/articles';

const MemberArticleService = {
  get(articleId) {
    return ApiService.get(`${BASE_URL}/${articleId}`);
  },
  getAll(params) {
    return ApiService.getWithParams(`${BASE_URL}`, params);
  }
};

export default MemberArticleService;
