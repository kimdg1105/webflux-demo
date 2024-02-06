package org.example.webfluxdemo.model;

public record PostCreateModel(
        String title,
        String content,
        String createdBy
) {
}
