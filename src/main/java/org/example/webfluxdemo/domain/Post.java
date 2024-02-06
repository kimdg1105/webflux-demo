package org.example.webfluxdemo.domain;

import lombok.*;
import org.example.webfluxdemo.model.PostCreateModel;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class Post {
    @Id
    private String id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private String createdBy;
    private boolean isDeleted;

    public static Post of(PostCreateModel model) {
        Post post = new Post();
        post.title = model.title();
        post.content = model.content();
        post.createdBy = model.createdBy();
        post.createdAt = LocalDateTime.now();
        post.isDeleted = false;
        return post;
    }
}
