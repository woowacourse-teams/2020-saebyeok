package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.*;
import com.saebyeok.saebyeok.dto.CommentCreateRequest;
import com.saebyeok.saebyeok.exception.ArticleNotFoundException;
import com.saebyeok.saebyeok.exception.CommentNotFoundException;
import com.saebyeok.saebyeok.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;

    @Transactional
    public Comment createComment(CommentCreateRequest commentCreateRequest) {
        Comment comment = toComment(commentCreateRequest);
        return commentRepository.save(comment);
    }

    private Comment toComment(CommentCreateRequest commentCreateRequest) {
        Comment comment = Comment.builder().
            content(commentCreateRequest.getContent()).
            nickname(commentCreateRequest.getNickname()).
            isDeleted(commentCreateRequest.getIsDeleted()).
            build();

        Long articleId = commentCreateRequest.getArticleId();
        Article article = articleRepository.findById(articleId).
            orElseThrow(() -> new ArticleNotFoundException(articleId));
        comment.setArticle(article);

        Long memberId = commentCreateRequest.getMemberId();
        Member member = memberRepository.findById(memberId).
            orElseThrow(() -> new MemberNotFoundException(memberId));
        comment.setMember(member);

        return comment;
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
