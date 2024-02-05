package org.example.webfluxdemo.service;

import org.example.webfluxdemo.domain.Image;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
interface ImageRepository extends ReactiveMongoRepository<Image, String> {
    Mono<Image> findByName(String name);
}
