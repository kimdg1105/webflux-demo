package org.example.webfluxdemo.service;

import lombok.RequiredArgsConstructor;
import org.example.webfluxdemo.domain.Post;
import org.example.webfluxdemo.dto.PostCreateDto;
import org.example.webfluxdemo.model.PostCreateModel;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public Mono<Post> create(PostCreateDto requestDto) {
        return postRepository.save(Post.of(
                        new PostCreateModel(
                                requestDto.getTitle(),
                                requestDto.getContent(),
                                requestDto.getCreatedBy()
                        )
                )
        );
    }

    public Flux<Post> getAll() {
        return postRepository.findAll();
    }
}
