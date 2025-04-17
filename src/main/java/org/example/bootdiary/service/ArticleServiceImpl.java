package org.example.bootdiary.service;

import org.example.bootdiary.model.entity.Article;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Override
    public List<Article> findAll() {
        return List.of();
    }
}
