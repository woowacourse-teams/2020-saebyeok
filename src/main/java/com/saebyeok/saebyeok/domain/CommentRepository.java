package com.saebyeok.saebyeok.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    public Long countCommentsByMemberAndIsDeleted(Member member, Boolean isDeleted);

    public List<Comment> findAllByMemberAndIsDeleted(Member member, Boolean isDeleted);
}
