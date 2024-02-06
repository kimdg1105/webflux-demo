package org.example.webfluxdemo.service;

import org.example.webfluxdemo.domain.Post;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

interface PostRepository extends ReactiveCrudRepository<Post, String> {
}
