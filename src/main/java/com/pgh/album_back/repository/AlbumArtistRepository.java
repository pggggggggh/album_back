package com.pgh.album_back.repository;

import com.pgh.album_back.entity.Album;
import com.pgh.album_back.entity.AlbumArtist;
import com.pgh.album_back.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumArtistRepository extends JpaRepository<AlbumArtist, Long> {
    boolean existsByAlbumAndArtist(Album album, Artist artist);
}
