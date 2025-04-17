package org.example.bootdiary.controller;

import lombok.extern.java.Log;
import org.example.bootdiary.exception.BadFileException;
import org.example.bootdiary.model.entity.Article;
import org.example.bootdiary.model.form.ArticleForm;
import org.example.bootdiary.service.ArticleService;
import org.example.bootdiary.service.FileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/article")
@Log
public class ArticleController {

    private final ArticleService articleService;
    private final FileService fileService;

    public ArticleController(ArticleService articleService, FileService fileService) {
        this.articleService = articleService;
        this.fileService = fileService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("title", "글 목록 📝");
        model.addAttribute("list", articleService.findAll());
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
    public String newArticle(ArticleForm form, Model model) {
        try {
            String filename = fileService.upload(form.file()); // 여기서 아예 에러가 터지게 하자!
            // 파일이 없다 -> 빈게 나옴. / 파일이 비었다 혹은 잘못된 파일이다 -> 예외처리 -> BadFileException

        } catch (BadFileException e) {
            model.addAttribute("message", "잘못된 파일");
            // 폼의 제목이랑 내용은 그대로 가져가고... 파일만 비워서 다시 폼으로 보냄
            model.addAttribute("form", new ArticleForm(form.title(), form.content(), null));
            return "redirect:/article/new";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "redirect:/article";
    }

}
