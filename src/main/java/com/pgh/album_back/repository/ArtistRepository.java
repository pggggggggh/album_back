package com.pgh.album_back.repository;

import com.pgh.album_back.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, String> {
    Artist findArtistByName(String name);

    Artist findArtistById(String Id);
}