package com.pgh.album_back.repository;

import com.pgh.album_back.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackRepository extends JpaRepository<Track, String> {
}