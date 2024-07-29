package com.pgh.album_back.controller;

import com.pgh.album_back.service.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/artists")
@RequiredArgsConstructor
public class ArtistController {
    private final ArtistService artistService;

    @GetMapping
    public ResponseEntity<Void> fetchArtist(String id) {
        artistService.fetchAndCreateArtist(id);
        return ResponseEntity.ok().build();
    }
}
