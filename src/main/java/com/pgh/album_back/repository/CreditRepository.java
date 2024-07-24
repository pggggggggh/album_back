package com.pgh.album_back.repository;

import com.pgh.album_back.entity.Artist;
import com.pgh.album_back.entity.Credit;
import com.pgh.album_back.entity.Entry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CreditRepository extends JpaRepository<Credit, Long> {
    Optional<Credit> findByArtistAndEntry(Artist artist, Entry entry);
}
