package com.pgh.album_back.repository;

import com.pgh.album_back.entity.AlbumTrack;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumTrackRepository extends JpaRepository<AlbumTrack,String> {
}
