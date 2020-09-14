package com.saebyeok.saebyeok.domain;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    public Optional<Article> findByIdAndCreatedDateGreaterThanEqualAndIsDeleted(Long id, LocalDateTime date, Boolean isDeleted);

    public List<Article> findAllByCreatedDateGreaterThanEqualAndIsDeleted(LocalDateTime date, Boolean isDeleted, Pageable pageable);

    public List<Article> findAllByCreatedDateGreaterThanEqualAndIsDeleted(LocalDateTime date, Boolean isDeleted);

    public List<Article> findAllByMemberAndIsDeleted(Member member, Boolean isDeleted, Pageable pageable);

    public Optional<Article> findByIdAndMemberEqualsAndIsDeleted(Long id, Member member, Boolean isDeleted);
}
