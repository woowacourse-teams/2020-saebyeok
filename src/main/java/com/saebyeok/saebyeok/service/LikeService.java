package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.*;
import com.saebyeok.saebyeok.exception.ArticleNotFoundException;
import com.saebyeok.saebyeok.exception.CommentNotFoundException;
import com.saebyeok.saebyeok.exception.DuplicateArticleLikeException;
import com.saebyeok.saebyeok.exception.DuplicateCommentLikeException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.saebyeok.saebyeok.service.ArticleService.FEED_LIMIT_DAYS;

@Transactional
@RequiredArgsConstructor
@Service
public class LikeService {

    private final ArticleLikeRepository articleLikeRepository;
    private final ArticleRepository articleRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;

    public ArticleLike likeArticle(Member member, Long articleId) {
        Article article = articleRepository.findByIdAndCreatedDateGreaterThanEqualAndIsDeleted(articleId, LocalDateTime.now().minusDays(FEED_LIMIT_DAYS), false)
                .orElseThrow(() -> new ArticleNotFoundException(articleId));

        ArticleLike like = new ArticleLike(member, article);

        try {
            return articleLikeRepository.save(like);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateArticleLikeException(member.getId(), articleId);
        }
    }

    public void unlikeArticle(Member member, Long articleId) {
        Article article = articleRepository.findByIdAndCreatedDateGreaterThanEqualAndIsDeleted(articleId, LocalDateTime.now().minusDays(FEED_LIMIT_DAYS), false)
                .orElseThrow(() -> new ArticleNotFoundException(articleId));

        articleLikeRepository.deleteByMemberAndArticle(member, article);
    }

    public CommentLike likeComment(Member member, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        CommentLike like = new CommentLike(member, comment);

        try {
            return commentLikeRepository.save(like);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateCommentLikeException(member.getId(), commentId);
        }
    }

    public void unlikeComment(Member member, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        commentLikeRepository.deleteByMemberAndComment(member, comment);
    }
}
