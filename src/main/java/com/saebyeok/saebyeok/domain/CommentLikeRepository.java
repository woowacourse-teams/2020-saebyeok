package com.saebyeok.saebyeok.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    void deleteByMemberAndComment(Member member, Comment comment);
}
