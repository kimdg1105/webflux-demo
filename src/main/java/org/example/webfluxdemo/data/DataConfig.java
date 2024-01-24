package org.example.webfluxdemo.data;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.example.webfluxdemo.constants.StringConstants.BASE_IMAGE_PATH;

@Configuration
public class DataConfig {
    @Bean
    CommandLineRunner setUp() {
        return args -> {
            FileSystemUtils.deleteRecursively(new File(BASE_IMAGE_PATH));
            Files.createDirectory((Paths.get(BASE_IMAGE_PATH)));
            FileCopyUtils.copy("Test file1", new FileWriter(BASE_IMAGE_PATH + "/test1.jpg"));
            FileCopyUtils.copy("Test file2", new FileWriter(BASE_IMAGE_PATH + "/test2.jpg"));
            FileCopyUtils.copy("Test file3", new FileWriter(BASE_IMAGE_PATH + "/test3.jpg"));
        };
    }
}
