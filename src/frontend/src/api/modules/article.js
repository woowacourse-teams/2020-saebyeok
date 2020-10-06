import ApiService from '@/api';

const BASE_URL = '/articles';

const ArticleService = {
  create(article) {
    return ApiService.post(`${BASE_URL}`, article);
  },
  get(articleId) {
    return ApiService.get(`${BASE_URL}/${articleId}`);
  },
  getAll(params) {
    return ApiService.getWithParams(`${BASE_URL}`, params);
  },
  delete(articleId) {
    return ApiService.delete(`${BASE_URL}/${articleId}`);
  },
  like(articleId) {
    return ApiService.post(`${BASE_URL}/${articleId}/likes`);
  },
  unlike(articleId) {
    return ApiService.delete(`${BASE_URL}/${articleId}/likes`);
  }
};

export default ArticleService;
