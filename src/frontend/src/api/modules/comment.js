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
  }
};

export default CommentService;
