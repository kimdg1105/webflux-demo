package org.example.webfluxdemo.model;

import static org.example.webfluxdemo.constants.StringConstants.BASE_IMAGE_PATH;

public record ImageCreateModel(
        String name,
        String url
) {
    public static String createUrl(String fileName) {
        return "/" + BASE_IMAGE_PATH + "/" + fileName;
    }
}
