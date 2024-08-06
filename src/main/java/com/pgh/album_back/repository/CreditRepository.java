package com.pgh.album_back.repository;

import com.pgh.album_back.entity.Artist;
import com.pgh.album_back.entity.Credit;
import com.pgh.album_back.entity.Entry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditRepository extends JpaRepository<Credit, Long> {
    boolean existsByArtistAndEntry(Artist artist, Entry entry);
}