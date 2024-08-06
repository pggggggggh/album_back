package com.pgh.album_back.repository;

import com.pgh.album_back.entity.Artist;
import com.pgh.album_back.entity.Track;
import com.pgh.album_back.entity.TrackArtist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackArtistRepository extends JpaRepository<TrackArtist, Long> {
    boolean existsByTrackAndArtist(Track track, Artist artist);
}
