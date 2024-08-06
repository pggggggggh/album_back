package com.pgh.album_back.repository;

import com.pgh.album_back.entity.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Album, String> {
    Page<Album> findAllByOrderByDateDesc(Pageable pageable);
    boolean existsById(String id);
}
