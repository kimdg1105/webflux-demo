package org.example.webfluxdemo.service;

import lombok.RequiredArgsConstructor;
import org.example.webfluxdemo.domain.Image;
import org.example.webfluxdemo.model.ImageCreateModel;
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
    private final ImageRepository imageRepository;
    private final ResourceLoader resourceLoader;

    public Flux<Image> getAll() {
        return imageRepository.findAll();
    }

    public Mono<Resource> getImage(String filename) {
        return Mono.fromSupplier(() -> resourceLoader.getResource("file:" + BASE_IMAGE_PATH + "/" + filename));
    }

    public Mono<Void> uploadImage(Flux<FilePart> files) {
        return files.flatMap(file -> {
                    Mono<Image> savedImage = imageRepository.save(
                            Image.of(new ImageCreateModel(file.filename(), ImageCreateModel.createUrl(file.filename()))));

                    Mono<Void> copyFile = Mono.just(
                                    Paths.get(BASE_IMAGE_PATH, file.filename()).toFile())
                            .log("createImage-pick-target")
                            .map(destFile -> {
                                try {
                                    destFile.createNewFile();
                                    return destFile;
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            })
                            .log("createImage-newfile")
                            .flatMap(file::transferTo)
                            .log("createImage-copy");
                    return Mono.when(savedImage, copyFile);
                })
                .then();
    }

    public Mono<Void> deleteImage(String filename) {
        Mono<Void> deleteEntityMono = imageRepository.findByName(filename)
                .flatMap(imageRepository::delete);

        Mono<Object> deletFileMono = Mono.fromRunnable(() -> {
            try {
                Files.deleteIfExists(Paths.get(BASE_IMAGE_PATH, filename));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        return Mono.when(deleteEntityMono, deletFileMono).then();
    }

}
