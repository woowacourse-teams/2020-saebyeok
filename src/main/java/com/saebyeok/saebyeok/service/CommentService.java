package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.Comment;
import com.saebyeok.saebyeok.domain.CommentRepository;
import com.saebyeok.saebyeok.dto.CommentCreateRequest;
import com.saebyeok.saebyeok.exception.CommentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;

    @Transactional
    public Comment createComment(CommentCreateRequest commentCreateRequest) {
        Comment comment = toComment(commentCreateRequest);
        return commentRepository.save(comment);
    }

    private Comment toComment(CommentCreateRequest commentCreateRequest) {
        return Comment.builder().
            content(commentCreateRequest.getContent()).
            nickname(commentCreateRequest.getNickname()).
            isDeleted(commentCreateRequest.getIsDeleted()).
            build();
    }

    @Transactional
    public void deleteComment(Long id) {
        try {
            commentRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new CommentNotFoundException(id);
        }
    }
}
