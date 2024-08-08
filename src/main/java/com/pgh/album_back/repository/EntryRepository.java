package com.pgh.album_back.repository;

import com.pgh.album_back.entity.Entry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntryRepository extends JpaRepository<Entry,String> {
}
