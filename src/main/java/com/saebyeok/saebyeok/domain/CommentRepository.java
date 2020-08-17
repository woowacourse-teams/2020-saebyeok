package com.saebyeok.saebyeok.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    public Long countCommentsByMember(Member member);
}
