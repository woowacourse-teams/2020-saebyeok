package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.*;
import com.saebyeok.saebyeok.dto.CommentCreateRequest;
import com.saebyeok.saebyeok.dto.CommentResponse;
import com.saebyeok.saebyeok.exception.ArticleNotFoundException;
import com.saebyeok.saebyeok.exception.CommentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentService {
    private static final String NOT_YOUR_COMMENT_MESSAGE = "자신의 댓글이 아닙니다!";

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final NicknameGenerator nicknameGenerator;

    public List<CommentResponse> getComment(Member member, Long articleId) {
        List<Comment> comments = commentRepository.findAllByArticleId(articleId);
        return comments.
                stream().
                sorted().
                map(comment -> new CommentResponse(comment, member)).
                collect(Collectors.toList());
    }

    @Transactional
    public Comment createComment(Member member, CommentCreateRequest commentCreateRequest) {
        Long articleId = commentCreateRequest.getArticleId();
        Article article = articleRepository.findById(articleId).
                orElseThrow(() -> new ArticleNotFoundException(articleId));
        Long parentId = commentCreateRequest.getParentId();
        Comment parent;
        if (parentId != null) {
            parent = commentRepository.findById(parentId).
                    orElseThrow(() -> new CommentNotFoundException(parentId));
        } else {
            parent = null;
        }

        String nickname = nicknameGenerator.generate(member, article);
        Comment comment = commentCreateRequest.toComment(member, nickname, article, parent);
        return commentRepository.save(comment);
    }

    public Long countTotalCommentsBy(Member member) {
        return commentRepository.countCommentsByMemberAndIsDeleted(member, false);
    }

    public List<Comment> findAllCommentsBy(Member member) {
        return commentRepository.findAllByMemberAndIsDeleted(member, false);
    }

    @Transactional
    public void deleteComment(Member member, Long commentId) throws IllegalAccessException {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));
        if (!comment.isWrittenBy(member)) {
            throw new IllegalAccessException(NOT_YOUR_COMMENT_MESSAGE);
        }
        comment.delete();
        commentRepository.save(comment);
    }
}
