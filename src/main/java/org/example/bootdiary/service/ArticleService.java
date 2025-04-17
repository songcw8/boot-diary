package org.example.bootdiary.service;

import org.example.bootdiary.exception.BadDataException;
import org.example.bootdiary.exception.BadFileException;
import org.example.bootdiary.model.entity.Article;

import java.util.List;

public interface ArticleService {

    //List<Article> findAll();
    List<Article> findAllByOrderByTitleDesc();

    void save(Article article) throws BadDataException;

    Article findById(String uuid);

    void deleteById(String uuid);
}
