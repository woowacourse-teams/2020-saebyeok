import ApiService from '@/api';

const BASE_URL = '/articles';

const CommentService = {
  create(commentRequest) {
    return ApiService.post(
      `${BASE_URL}/${commentRequest.articleId}/comments`,
      commentRequest
    );
  },
  get(articleId) {
    return ApiService.get(`${BASE_URL}/${articleId}/comments`);
  },
  delete(params) {
    return ApiService.delete(
      `${BASE_URL}/${params.articleId}/comments/${params.commentId}`
    );
  },
  like(params) {
    return ApiService.post(
      `${BASE_URL}/${params.articleId}/comments/${params.commentId}/likes`
    );
  },
  unlike(params) {
    return ApiService.delete(
      `${BASE_URL}/${params.articleId}/comments/${params.commentId}/likes`
    );
  }
};

export default CommentService;
