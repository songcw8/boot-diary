package org.example.bootdiary.model.repository;

import org.example.bootdiary.model.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, String> {
    List<Article> findAllByOrderByTitleDesc(); // 최신순 정렬
    //List<Article> findAllByOrderByTitle(); // 오래된 순 정렬
}
