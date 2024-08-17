package com.pgh.album_back.service;

import com.pgh.album_back.entity.Thumbnail;
import com.pgh.album_back.repository.ThumbnailRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ThumbnailService {
    private final WebClient webClient;
    private final ThumbnailRepository thumbnailRepository;

    public ThumbnailService(@Qualifier("CoverArtWebClient") WebClient webClient, ThumbnailRepository thumbnailRepository) {
        this.webClient = webClient;
        this.thumbnailRepository = thumbnailRepository;
    }

    @Transactional
    public Thumbnail downloadThumbnail(String url) {
        byte[] bytes = webClient.get().uri(url).accept(MediaType.APPLICATION_OCTET_STREAM)
                .retrieve().bodyToMono(byte[].class).blockOptional().orElseThrow();

        String originalFilename = UUID.randomUUID().toString() + ".png";
        Path path = Paths.get("/images", originalFilename);

        try {
            if (Files.notExists(path.getParent())) {
                Files.createDirectories(path.getParent());
            }
            Files.write(path, bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Thumbnail thumbnail = new Thumbnail();
        thumbnail.setImagePath(path.toString());

        thumbnailRepository.save(thumbnail);
        return thumbnail;
    }
}