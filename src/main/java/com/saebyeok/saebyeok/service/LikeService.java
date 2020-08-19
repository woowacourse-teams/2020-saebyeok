package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.*;
import com.saebyeok.saebyeok.exception.ArticleNotFoundException;
import com.saebyeok.saebyeok.exception.DuplicateArticleLikeException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.saebyeok.saebyeok.service.ArticleService.LIMIT_DAYS;

@RequiredArgsConstructor
@Service
public class LikeService {

    private final ArticleLikeRepository articleLikeRepository;
    private final ArticleRepository articleRepository;

    public ArticleLike likeArticle(Member member, Long articleId) {
        Article article = articleRepository.findByIdAndCreatedDateGreaterThanEqual(articleId, LocalDateTime.now().minusDays(LIMIT_DAYS))
                .orElseThrow(() -> new ArticleNotFoundException(articleId));

        ArticleLike like = new ArticleLike(member, article);

        try {
            return articleLikeRepository.save(like);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateArticleLikeException(member.getId(), articleId);
        }
    }

    public CommentLike likeComment(Member member, Long commentId) {
        return null;
    }
}
