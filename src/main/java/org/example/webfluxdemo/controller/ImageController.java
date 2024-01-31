package org.example.webfluxdemo.controller;

import lombok.RequiredArgsConstructor;
import org.example.webfluxdemo.service.ImageService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class ImageController {
    private static final String BASE_PATH = "/images";
    private static final String FILE_NAME = "{filename:.+}";

    private final ImageService imageService;

    @GetMapping(value = BASE_PATH + "/view/" + FILE_NAME, produces = MediaType.IMAGE_JPEG_VALUE)
    public Mono<ResponseEntity<?>> getImage(@PathVariable String filename) {
        return imageService.getImage(filename)
                .map(resource -> {
                    try {
                        return ResponseEntity.ok()
                                .contentLength(resource.contentLength())
                                .body(new InputStreamResource(resource.getInputStream()));
                    } catch (Exception e) {
                        return ResponseEntity.badRequest().body("Couldn't find " + filename + " => " + e.getMessage());
                    }
                });
    }

    @PostMapping(BASE_PATH)
    public Mono<String> createFile(@RequestPart(name = "file") Flux<FilePart> files) {
        return imageService.uploadImage(files)
                .then(Mono.just("redirect:/"));
    }

    @DeleteMapping(value = BASE_PATH + "/delete" + "/{filename}")
    public Mono<String> deleteFile(@PathVariable String filename) {
        return imageService.deleteImage(filename)
                .then(Mono.just("redirect:/"));
    }

    @GetMapping("/")
    public Mono<String> index(Model model) {
        model.addAttribute("images", imageService.getAll());
        return Mono.just("index");
    }

}
