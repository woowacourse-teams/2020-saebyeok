package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.Comment;
import com.saebyeok.saebyeok.domain.CommentRepository;
import com.saebyeok.saebyeok.dto.CommentCreateRequest;
import com.saebyeok.saebyeok.exception.InvalidCommentException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {
    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 140;

    private CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Transactional
    public Comment createComment(CommentCreateRequest commentCreateRequest) {
        validate(commentCreateRequest);

        Comment comment = toComment(commentCreateRequest);
        return commentRepository.save(comment);
    }

    private void validate(CommentCreateRequest commentCreateRequest) {
        int contentLength = commentCreateRequest.getContent().trim().length();
        if (contentLength < MIN_LENGTH) {
            throw new InvalidCommentException(String.format("댓글의 최소 길이는 %d글자입니다!", MIN_LENGTH));
        }
        if (contentLength > MAX_LENGTH) {
            throw new InvalidCommentException(String.format("댓글의 최대 길이는 %d자입니다!", MAX_LENGTH));
        }
    }

    private Comment toComment(CommentCreateRequest commentCreateRequest) {
        return Comment.builder().
            content(commentCreateRequest.getContent()).
            member(commentCreateRequest.getMember()).
            nickname(commentCreateRequest.getNickname()).
            article(commentCreateRequest.getArticle()).
            isDeleted(commentCreateRequest.getIsDeleted()).
            build();
    }

    @Transactional
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
