package com.saebyeok.saebyeok.domain;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    public Optional<Article> findByIdAndCreatedDateGreaterThanEqual(Long id, LocalDateTime date);

    public List<Article> findAllByCreatedDateGreaterThanEqual(LocalDateTime date, Pageable pageable);

    public List<Article> findAllByMember(Member member, Pageable pageable);

    public Optional<Article> findByIdAndMemberEquals(Long id, Member member);
}
