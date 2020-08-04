package com.saebyeok.saebyeok.domain;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    public List<Article> findAllByOrderByIdDesc();

    public List<Article> findAllByCreatedDateGreaterThanEqual(LocalDateTime date, Pageable pageable);
}
