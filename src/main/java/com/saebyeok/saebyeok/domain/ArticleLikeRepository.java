package com.saebyeok.saebyeok.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Long> {
    void deleteByMemberAndArticle(Member member, Article article);
}
