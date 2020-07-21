package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.*;
import com.saebyeok.saebyeok.dto.CommentCreateRequest;
import com.saebyeok.saebyeok.exception.ArticleNotFoundException;
import com.saebyeok.saebyeok.exception.CommentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;

    @Transactional
    public Comment createComment(Member member, CommentCreateRequest commentCreateRequest) {
        // Todo: 초기 개발을 위한 member 생성 로직 삭제하고 security 적용하면 member 받아오기
        Member test = new Member(1L, 1991, Gender.FEMALE, LocalDateTime.now(), false, new ArrayList<>());

        Long articleId = commentCreateRequest.getArticleId();
        Article article = articleRepository.findById(articleId).
                orElseThrow(() -> new ArticleNotFoundException(articleId));

        Comment comment = commentCreateRequest.toComment();
        comment.setArticle(article);
        comment.setMember(test);

        return commentRepository.save(comment);
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
