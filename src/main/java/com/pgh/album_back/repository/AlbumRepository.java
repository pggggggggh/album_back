package com.pgh.album_back.repository;

import com.pgh.album_back.entity.Album;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlbumRepository extends JpaRepository<Album, String> {
    List<Album> findAllByOrderByDateDesc(Pageable pageable);
}
