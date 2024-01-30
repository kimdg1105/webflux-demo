package org.example.webfluxdemo.data;

import lombok.extern.slf4j.Slf4j;
import org.example.webfluxdemo.domain.Image;
import org.example.webfluxdemo.model.ImageCreateModel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataConfig {


    @Bean
    CommandLineRunner setUp(MongoOperations operations) {
        return args -> {
            operations.insert(Image.of((new ImageCreateModel("test1.jpg", "/images/test1.jpg"))));
            operations.insert(Image.of((new ImageCreateModel("test2.jpg", "/images/test2.jpg"))));
            operations.insert(Image.of((new ImageCreateModel("test3.jpg", "/images/test3.jpg"))));
            operations.insert(Image.of((new ImageCreateModel("test4.jpg", "/images/test4.jpg"))));

            operations.findAll(Image.class).forEach(image -> {
                log.info("Image: {}", image.toString());
            });
        };
    }
}
