package com.pgh.album_back.repository;

import com.pgh.album_back.entity.Album;
import com.pgh.album_back.entity.AlbumTrack;
import com.pgh.album_back.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumTrackRepository extends JpaRepository<AlbumTrack,String> {
    boolean existsByAlbumAndTrack(Album album, Track track);
}
