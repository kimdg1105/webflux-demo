package org.example.webfluxdemo.domain;

import lombok.*;
import org.example.webfluxdemo.model.ImageCreateModel;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@Document
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@ToString
public class Image {
    @Id
    private String id;
    private String name;
    private String url;

    public static Image of(ImageCreateModel model) {
        Image image = new Image();
        image.id = UUID.randomUUID().toString();
        image.name = model.name();
        image.url = model.url();
        return image;
    }

}