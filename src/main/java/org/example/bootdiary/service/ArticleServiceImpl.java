package org.example.bootdiary.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.bootdiary.exception.BadDataException;
import org.example.bootdiary.model.entity.Article;
import org.example.bootdiary.model.repository.ArticleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;

//    @Override
//    public List<Article> findALl() {
//        return articleRepository.findAll();
//    }

    @Override
    public List<Article> findAllByOrderByTitleDesc() {
        return articleRepository.findAllByOrderByTitleDesc();
    }

    @Override
    public void save(Article article) throws BadDataException {
        if (article.getTitle().isEmpty()) {
            throw new BadDataException("제목이 없습니다.");
        }
        if (article.getContent().isEmpty()) {
            throw new BadDataException("내용이 없습니다");
        }
        articleRepository.save(article);
    }

    @Override
    public Article findById(String uuid) {
        return articleRepository.findById(uuid).orElseThrow();
    }

    @Override
    public void deleteById(String uuid) {
        articleRepository.deleteById(uuid);
    }

}
