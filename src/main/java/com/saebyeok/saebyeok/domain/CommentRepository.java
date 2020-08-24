package com.saebyeok.saebyeok.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    public Long countCommentsByMember(Member member);

    public List<Comment> findAllByMember(Member member);
}
