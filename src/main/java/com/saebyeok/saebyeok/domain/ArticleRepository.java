package com.saebyeok.saebyeok.domain;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    public Optional<Article> findByIdAndCreatedDateGreaterThanEqual(Long id, LocalDateTime date);

    public List<Article> findAllByCreatedDateGreaterThanEqual(LocalDateTime date, Pageable pageable);

    @Query(value = "select ARTICLE_ID from ARTICLE where MEMBER_ID = :memberId", nativeQuery = true)
    public List<Long> findArticlesIdsByMemberId(@Param("memberId") Long memberId);

    @Query(value = "select ARTICLE_ID from ARTICLE where MEMBER_ID = :memberId and CREATED_DATE >= :cutDate",
            nativeQuery = true)
    public List<Long> findArticlesIdsByMemberIdAndCutDate(@Param("memberId") Long memberId,
                                                          @Param("cutDate") LocalDateTime date);
}
