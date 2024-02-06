package org.example.webfluxdemo.dto;


import lombok.Data;

@Data
public class PostCreateDto {
    private String title;
    private String content;
    private String createdBy;
}

