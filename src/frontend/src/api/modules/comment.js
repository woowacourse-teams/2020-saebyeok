import ApiService from '@/api';

const BASE_URL = '/articles';

const CommentService = {
  create(commentRequest) {
    return ApiService.post(
      `${BASE_URL}/${commentRequest.articleId}/comments`,
      commentRequest
    );
  },
  delete(articleId, commentId) {
    return ApiService.delete(`${BASE_URL}/${articleId}/comments/${commentId}`);
  }
};

export default CommentService;
