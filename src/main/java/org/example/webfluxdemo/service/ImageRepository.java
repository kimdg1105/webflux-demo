package org.example.webfluxdemo.service;

import org.example.webfluxdemo.domain.Image;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

interface ImageRepository extends ReactiveMongoRepository<Image, String> {
    Mono<Image> findByName(String name);
}
