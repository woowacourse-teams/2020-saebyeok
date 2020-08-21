package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.*;
import com.saebyeok.saebyeok.dto.CommentCreateRequest;
import com.saebyeok.saebyeok.exception.ArticleNotFoundException;
import com.saebyeok.saebyeok.exception.CommentNotFoundException;
import com.saebyeok.saebyeok.util.NicknameGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CommentService {
    private static final String NOT_YOUR_COMMENT_MESSAGE = "자신의 댓글이 아닙니다!";

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final NicknameGenerator nicknameGenerator;

    @Transactional
    public Comment createComment(Member member, CommentCreateRequest commentCreateRequest) {
        Long articleId = commentCreateRequest.getArticleId();
        Article article = articleRepository.findById(articleId).
                orElseThrow(() -> new ArticleNotFoundException(articleId));

        Comment comment = commentCreateRequest.toComment();
        comment.setArticle(article);
        comment.setMember(member);
        comment.setNickname(nicknameGenerator.generate(member, article));

        return commentRepository.save(comment);
    }

    public Long countTotalCommentsBy(Member member) {
        return commentRepository.countCommentsByMember(member);
    }

    @Transactional
    public void deleteComment(Member member, Long commentId) throws IllegalAccessException {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));
        if (!comment.isWrittenBy(member)) {
            throw new IllegalAccessException(NOT_YOUR_COMMENT_MESSAGE);
        }
        commentRepository.deleteById(commentId);
    }
}
