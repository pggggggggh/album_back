package com.pgh.album_back.repository;

import com.pgh.album_back.entity.Entry;
import com.pgh.album_back.entity.Review;
import com.pgh.album_back.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    List<Review> findAllByEntryOrderByCreatedAtDesc(Entry entry);
    Optional<Review> findFirstByUserAndEntry(User user,Entry entry);
    boolean existsByUserAndEntry(User user,Entry entry);
    Page<Review> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
