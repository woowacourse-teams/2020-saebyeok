import ApiService from '@/api';

const BASE_URL = '/articles';

const CommentService = {
  create(commentRequest) {
    return ApiService.post(
      `${BASE_URL}/${commentRequest.articleId}/comments`,
      commentRequest
    );
  },
  delete(params) {
    return ApiService.delete(
      `${BASE_URL}/${params.articleId}/comments/${params.commentId}`
    );
  },
  like(commentId) {
    return ApiService.post(`/likes/comment/${commentId}`);
  },
  unlike(commentId) {
    return ApiService.delete(`/likes/comment/${commentId}`);
  }
};

export default CommentService;
