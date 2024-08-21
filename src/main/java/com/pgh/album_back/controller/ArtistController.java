package com.pgh.album_back.controller;

import com.pgh.album_back.repository.ArtistRepository;
import com.pgh.album_back.service.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/artists")
@RequiredArgsConstructor
public class ArtistController {
    private final ArtistService artistService;
    private final ArtistRepository artistRepository;

//    @Secured("ROLE_ADMIN")
    @PostMapping
    public ResponseEntity<Void> fetchArtist(
            @RequestBody List<String> artistIds
    ) {
        for (var id:artistIds) {
            if (!artistRepository.existsById(id)) artistService.fetchAndCreateArtist(id);
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteArtist(
            @RequestBody List<String> artistIds
    ) {
        for (var id:artistIds) {
            if (artistRepository.existsById(id)) artistRepository.deleteById(id);
        }
        return ResponseEntity.ok().build();
    }
}