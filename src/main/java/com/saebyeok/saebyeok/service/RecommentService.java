package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.*;
import com.saebyeok.saebyeok.dto.RecommentCreateRequest;
import com.saebyeok.saebyeok.exception.ArticleNotFoundException;
import com.saebyeok.saebyeok.exception.CommentNotFoundException;
import com.saebyeok.saebyeok.util.NicknameGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class RecommentService {
    private final RecommentRepository recommentRepository;
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final NicknameGenerator nicknameGenerator;

    public Recomment createRecomment(Member member, RecommentCreateRequest recommentCreateRequest) {
        Recomment recomment = toRecomment(member, recommentCreateRequest);
        return recommentRepository.save(recomment);
    }

    private Recomment toRecomment(Member member, RecommentCreateRequest recommentCreateRequest) {
        Long articleId = recommentCreateRequest.getArticleId();
        Article article = articleRepository.findById(articleId).
                orElseThrow(() -> new ArticleNotFoundException(articleId));
        Long commentId = recommentCreateRequest.getCommentId();
        Comment comment = commentRepository.findById(commentId).
                orElseThrow(() -> new CommentNotFoundException(commentId));
        return Recomment.builder().content(recommentCreateRequest.getContent())
                .member(member)
                .nickname(nicknameGenerator.generate(member, article))
                .article(article)
                .comment(comment)
                .build();
    }
}
