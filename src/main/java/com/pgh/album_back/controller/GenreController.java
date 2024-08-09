package com.pgh.album_back.controller;

import com.pgh.album_back.dto.AddGenreDTO;
import com.pgh.album_back.entity.Genre;
import com.pgh.album_back.repository.GenreRepository;
import com.pgh.album_back.service.GenreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/genres")
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;
    private final GenreRepository genreRepository;

    @Secured("ROLE_ADMIN")
    @PostMapping
    public ResponseEntity<?> addGenre(@RequestBody @Valid AddGenreDTO genreDto) {
        genreService.addGenre(genreDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<?> getAllGenreNames() {
        return ResponseEntity.ok(genreRepository.findAll().stream().map(Genre::getName).toList());
    }
}
