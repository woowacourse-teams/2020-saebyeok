package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.*;
import com.saebyeok.saebyeok.dto.CommentCreateRequest;
import com.saebyeok.saebyeok.exception.ArticleNotFoundException;
import com.saebyeok.saebyeok.exception.CommentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CommentService {
    public static final String NOT_YOUR_COMMENT = "자신의 댓글이 아닙니다!";

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;

    @Transactional
    public Comment createComment(Member member, CommentCreateRequest commentCreateRequest) {
        Long articleId = commentCreateRequest.getArticleId();
        Article article = articleRepository.findById(articleId).
                orElseThrow(() -> new ArticleNotFoundException(articleId));

        Comment comment = commentCreateRequest.toComment();
        comment.setArticle(article);
        comment.setMember(member);

        return commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Member member, Long id) throws IllegalAccessException {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException(id));
        if (!comment.isWrittenBy(member)) {
            throw new IllegalAccessException(NOT_YOUR_COMMENT);
        }
        commentRepository.deleteById(id);
    }
}
