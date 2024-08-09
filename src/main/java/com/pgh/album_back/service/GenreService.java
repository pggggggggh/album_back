package com.pgh.album_back.service;

import com.pgh.album_back.dto.AddGenreDTO;
import com.pgh.album_back.entity.Genre;
import com.pgh.album_back.repository.GenreRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;

    public void addGenre(AddGenreDTO genreDto) {
        Genre genre = new Genre();
        if (genreRepository.existsByNameIgnoreCase(genreDto.getName())) {
            throw new DataIntegrityViolationException("Genre already exists");
        }
        genre.setName(genreDto.getName());

        genre.setDescription(genreDto.getDescription());

        if (genreDto.getParentGenre() != null && !genreDto.getParentGenre().isEmpty()) {
            Genre parentGenre = genreRepository.findByNameIgnoreCase(genreDto.getParentGenre())
                    .orElseThrow(() -> new EntityNotFoundException("Genre not found"));
            genre.setParentGenre(parentGenre);
            parentGenre.addSubgenre(genre);
        }
        genreRepository.save(genre);
    }
}
