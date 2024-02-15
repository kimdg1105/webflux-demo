package org.example.webfluxdemo.controller;

import lombok.RequiredArgsConstructor;
import org.example.webfluxdemo.dto.PostCreateDto;
import org.example.webfluxdemo.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping
    public Mono<String> index(Model model) {
        model.addAttribute("requestDto", new PostCreateDto());
        model.addAttribute("posts", postService.getAll());
        return Mono.just("posts");
    }

    @PostMapping
    public Mono<String> create(@ModelAttribute PostCreateDto requestDto) {
        return postService.create(requestDto)
                .then(Mono.just("redirect:/posts"));
    }
}
