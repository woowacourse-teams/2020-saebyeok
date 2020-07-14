package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.Comment;
import com.saebyeok.saebyeok.dto.CommentCreateRequest;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    public Comment createComment(CommentCreateRequest commentCreateRequest) {
        return new Comment();
    }

    public void deleteComment(Long articleId, Long commentId) {

    }
}
