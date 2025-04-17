package org.example.bootdiary.controller;

import lombok.extern.java.Log;
import org.example.bootdiary.exception.BadDataException;
import org.example.bootdiary.exception.BadFileException;
import org.example.bootdiary.model.entity.Article;
import org.example.bootdiary.model.form.ArticleForm;
import org.example.bootdiary.service.AIService;
import org.example.bootdiary.service.AIServiceImpl;
import org.example.bootdiary.service.ArticleService;
import org.example.bootdiary.service.FileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/article")
@Log
public class ArticleController {

    private final ArticleService articleService;
    private final FileService fileService;
    private final AIServiceImpl aIServiceImpl;
    private final AIService aIService;

    public ArticleController(ArticleService articleService, FileService fileService, AIServiceImpl aIServiceImpl, AIService aIService) {
        this.articleService = articleService;
        this.fileService = fileService;
        this.aIServiceImpl = aIServiceImpl;
        this.aIService = aIService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("title", "글 목록 📝");
        model.addAttribute("list", articleService.findAllByOrderByTitleDesc());
        return "article/list";
    }

    @GetMapping("/new")
    public String newArticle(Model model) {
        model.addAttribute("title", "글 작성 ✏️");
        model.addAttribute("form", ArticleForm.empty());
        model.addAttribute("edit", false);
        return "article/form";
    }

    @PostMapping("/new")
    public String newArticle(ArticleForm form, Model model, RedirectAttributes redirectAttributes) {
        try {
            String filename = fileService.upload(form.file()); // 여기서 아예 에러가 터지게 하자!
            // 파일이 없다 -> 빈게 나옴. / 파일이 비었다 혹은 잘못된 파일이다 -> 예외처리 -> BadFileException
            Article article = new Article();
            article.setTitle(form.title());
            article.setContent(form.content());
            article.setFilename(filename);
            articleService.save(article);
            String result = aIService.answer(form.title() + """
                    사용자가 아래에 입력한 텍스트를 참고하여 그에 어울리는 동기부여 명언 하나를 선정해줘. 명언은 사람의 감정을 북돋을 수 있고, 상황에 맞는 힘이 되어야 해. 명언은 반드시 실제 인물의 말이어야 하며, 다음 형식을 따라줘:
                    명언 -발언자
                    명언은 마크다운이 아니라 일반 평문으로 출력하고, 다른 설명이나 해석은 덧붙이지 마.
                    
                    """);
            log.info(result);
            redirectAttributes.addFlashAttribute("result", result);
        } catch (BadFileException e) {
//            model.addAttribute("message", "잘못된 파일");
            model.addAttribute("message", e.getMessage());
            // 폼의 제목이랑 내용은 그대로 가져가고... 파일만 비워서 다시 폼으로 보냄
            model.addAttribute("form", new ArticleForm(form.title(), form.content(), null));
//            return "redirect:/article/new";
            return "article/form";
        } catch (BadDataException e) {
            model.addAttribute("message", e.getMessage());
            model.addAttribute("form", form);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "redirect:/article";
    }

    @GetMapping("/edit/{uuid}")
    public String editArticle(Model model, @PathVariable String uuid) {
        model.addAttribute("title", "글 작성 ✏️");
        Article prevArticle = articleService.findById(uuid);
        model.addAttribute("form", new ArticleForm(prevArticle.getTitle(), prevArticle.getContent(), null));
        model.addAttribute("edit", true);
        return "article/form";
    }

    @PostMapping("/edit/{uuid}")
    public String editArticle(ArticleForm form, Model model, @PathVariable String uuid) {
        try {
            String filename = fileService.upload(form.file()); // 여기서 아예 에러가 터지게 하자!
            // 파일이 없다 -> 빈게 나옴. / 파일이 비었다 혹은 잘못된 파일이다 -> 예외처리 -> BadFileException
            Article article = articleService.findById(uuid);
            article.setTitle(form.title());
            article.setContent(form.content());
            if (!filename.isEmpty()) {
                article.setFilename(filename);
            }
            articleService.save(article);
        } catch (BadFileException e) {
            model.addAttribute("message", e.getMessage());
            // 폼의 제목이랑 내용은 그대로 가져가고... 파일만 비워서 다시 폼으로 보냄
            model.addAttribute("form", new ArticleForm(form.title(), form.content(), null));
            return "article/form";
        } catch (BadDataException e) {
            model.addAttribute("message", e.getMessage());
            model.addAttribute("form", form);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "redirect:/article";
    }

    @PostMapping("/delete/{uuid}")
    public String deleteArticle(@PathVariable String uuid, RedirectAttributes redirectAttributes) {
        articleService.deleteById(uuid);
        redirectAttributes.addFlashAttribute("message", "삭제 성공! %s".formatted(uuid));
        return "redirect:/article";
    }
}
