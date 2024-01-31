package org.example.webfluxdemo.config;

import lombok.extern.slf4j.Slf4j;
import org.example.webfluxdemo.domain.Image;
import org.example.webfluxdemo.model.ImageCreateModel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.filter.reactive.HiddenHttpMethodFilter;

@Slf4j
@Configuration
public class AppConfig {
    @Bean
    HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }

    @Bean
    CommandLineRunner setUp(MongoOperations operations) {
        return args -> {
            operations.findAllAndRemove(new Query(), Image.class);

            operations.insert(Image.of((new ImageCreateModel("1.jpeg", "/images/1.jpeg"))));
            operations.insert(Image.of((new ImageCreateModel("2.jpeg", "/images/2.jpeg"))));
            operations.insert(Image.of((new ImageCreateModel("3.jpeg", "/images/3.jpeg"))));

            operations.findAll(Image.class).forEach(image -> log.info("Image: {}", image.toString()));
        };
    }
}
