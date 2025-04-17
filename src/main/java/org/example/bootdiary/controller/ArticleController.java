package org.example.bootdiary.controller;

import org.example.bootdiary.model.entity.Article;
import org.example.bootdiary.model.form.ArticleForm;
import org.example.bootdiary.service.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/article")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
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
    public String newArticle(ArticleForm form, RedirectAttributes redirectAttributes, Model model) {
        Article article = new Article();
        article.setTitle(form.title());
        article.setContent(form.content());
        try {
            //TODO : file
            articleService.save(article);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("form", form);
            return "article/form";
        }
        redirectAttributes.addFlashAttribute("message", "추가 성공!");
        return "redirect:/article";
    }
}
