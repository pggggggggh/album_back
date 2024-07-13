package com.pgh.album_back.Service;

import com.pgh.album_back.Entity.Artist;
import com.pgh.album_back.Entity.ArtistRelationship;
import com.pgh.album_back.Repository.ArtistRelationshipRepository;
import com.pgh.album_back.Repository.ArtistRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArtistService {
    private final ArtistRepository artistRepository;
    private final ArtistRelationshipRepository artistRelationshipRepository;

    @Transactional
    public void saveArtist(Artist artist) {
        artistRepository.save(artist);
    }
    @Transactional
    public void addArtistToGroup(Long memberId, Long groupId) {
        Artist member = artistRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);
        Artist group = artistRepository.findById(groupId).orElseThrow(EntityNotFoundException::new);

        ArtistRelationship artistRelationship = member.addToGroup(group);
        artistRepository.save(member);
        artistRepository.save(group);
        artistRelationshipRepository.save(artistRelationship);
    }

    @Transactional
    public void removeArtistFromGroup(Long memberId, Long groupId) {
        Artist member = artistRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);
        Artist group = artistRepository.findById(groupId).orElseThrow(EntityNotFoundException::new);

        ArtistRelationship artistRelationship = member.removeFromGroup(group);
        artistRelationshipRepository.delete(artistRelationship);
    }
}
