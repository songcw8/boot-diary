package org.example.bootdiary.controller;

import org.example.bootdiary.model.entity.Article;
import org.example.bootdiary.service.FileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MainController {

    private final FileService fileService;

    public MainController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("title", "동기부여 앱");
        model.addAttribute("message", "브라키오 사우루스는 내가 이러는 걸 원치 않았을거야");
        model.addAttribute("frontImage", "/assets/jojo.jpeg");
        return "index";
    }

    @GetMapping("/file/{filename}")
    public ResponseEntity<byte[]> file(@PathVariable String filename) {
        try {
            byte[] fileBytes = fileService.download(filename); // 버킷 -> 파일 이름 요청

            // 페이지(템플릿)로 응답하지 않고, 데이터로 응답하겠다
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"").contentType(MediaType.APPLICATION_OCTET_STREAM).body(fileBytes);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
