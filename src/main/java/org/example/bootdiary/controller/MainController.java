package org.example.bootdiary.controller;

import org.example.bootdiary.model.entity.Article;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping
    public String index(Model model) {
        model.addAttribute("title", "동기부여 앱");
        model.addAttribute("message", "브라키오 사우루스는 내가 이러는 걸 원치 않았을거야");
        model.addAttribute("frontImage", "/assets/jojo.jpeg");
        return "index";
    }
}
