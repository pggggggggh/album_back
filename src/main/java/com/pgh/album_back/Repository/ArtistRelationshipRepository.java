package com.pgh.album_back.Repository;

import com.pgh.album_back.Entity.ArtistRelationship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRelationshipRepository extends JpaRepository<ArtistRelationship, Long> {
}