package org.example.webfluxdemo.service;

import lombok.RequiredArgsConstructor;
import org.example.webfluxdemo.domain.Image;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.example.webfluxdemo.constants.StringConstants.BASE_IMAGE_PATH;

@Service
@RequiredArgsConstructor
public class ImageService {


    private final ResourceLoader resourceLoader;

    public Flux<Image> getAllImages() {
        try {
            return Flux.fromIterable(Files.newDirectoryStream(Paths.get(BASE_IMAGE_PATH)))
                    .map(path ->
                            new Image(Integer.toString(path.hashCode()),
                                    path.getFileName().toString(), BASE_IMAGE_PATH + "/" + path.getFileName())
                    );
        } catch (IOException e) {
            return Flux.empty();
        }
    }

    public Mono<Resource> getImage(String filename) {
        return Mono.fromSupplier(() -> resourceLoader.getResource("file:" + BASE_IMAGE_PATH + "/" + filename));
    }

    public Mono<Void> uploadImage(Flux<FilePart> files) {
        return files.flatMap(file -> file.transferTo(Paths.get(BASE_IMAGE_PATH, file.filename()).toFile())).then();
    }

    public Mono<Void> deleteImage(String filename) {
        return Mono.fromRunnable(() -> {
            try {
                Files.deleteIfExists(Paths.get(BASE_IMAGE_PATH, filename));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
