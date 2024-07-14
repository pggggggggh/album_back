package com.pgh.album_back.repository;

import com.pgh.album_back.entity.Artist;
import com.pgh.album_back.entity.ArtistRelationship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArtistRelationshipRepository extends JpaRepository<ArtistRelationship, String> {
    boolean existsByMemberAndGroup(Artist Member, Artist Group);
    Optional<ArtistRelationship> findByMemberAndGroup(Artist Member, Artist Group);
}