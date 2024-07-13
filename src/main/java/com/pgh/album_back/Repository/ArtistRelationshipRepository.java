package com.pgh.album_back.Repository;

import com.pgh.album_back.Entity.Artist;
import com.pgh.album_back.Entity.ArtistRelationship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArtistRelationshipRepository extends JpaRepository<ArtistRelationship, Long> {
    boolean existsByMemberAndGroup(Artist Member, Artist Group);
    Optional<ArtistRelationship> findByMemberAndGroup(Artist Member, Artist Group);
}