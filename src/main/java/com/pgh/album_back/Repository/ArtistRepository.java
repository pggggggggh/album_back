package com.pgh.album_back.Repository;

import com.pgh.album_back.Entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
    Artist findArtistByName(String name);

    Artist findArtistById(Long Id);
}