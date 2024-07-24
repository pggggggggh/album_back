package com.pgh.album_back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class AlbumBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlbumBackApplication.class, args);
    }

}
