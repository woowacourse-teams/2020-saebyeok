package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.Comment;
import com.saebyeok.saebyeok.domain.CommentRepository;
import com.saebyeok.saebyeok.dto.CommentCreateRequest;
import com.saebyeok.saebyeok.exception.InvalidCommentException;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 140;

    private CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment createComment(CommentCreateRequest commentCreateRequest) {
        int contentLength = commentCreateRequest.getContent().trim().length();
        if (contentLength < MIN_LENGTH) {
            throw new InvalidCommentException("댓글의 최소 길이는 한 글자입니다!");
        }

        if (contentLength > MAX_LENGTH) {
            throw new InvalidCommentException("댓글의 최대 길이는 140자입니다!");
        }

        Comment comment = toComment(commentCreateRequest);
        return commentRepository.save(comment);
    }

    private Comment toComment(CommentCreateRequest commentCreateRequest) {
        return Comment.builder().
            content(commentCreateRequest.getContent()).
            member(commentCreateRequest.getMember()).
            nickname(commentCreateRequest.getNickname()).
            createdDate(commentCreateRequest.getCreatedDate()).
            article(commentCreateRequest.getArticle()).
            isDeleted(commentCreateRequest.getIsDeleted()).
            build();
    }

    public void deleteComment(Long articleId, Long commentId) {

    }
}
