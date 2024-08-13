package com.pgh.album_back.repository;

import com.pgh.album_back.entity.Thumbnail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThumbnailRepository extends JpaRepository<Thumbnail,Long> {
}
