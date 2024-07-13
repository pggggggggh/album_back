package com.pgh.album_back.Service;

import com.pgh.album_back.Entity.Artist;
import com.pgh.album_back.Entity.ArtistRelationship;
import com.pgh.album_back.Repository.ArtistRelationshipRepository;
import com.pgh.album_back.Repository.ArtistRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
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

        if (artistRelationshipRepository.existsByMemberAndGroup(member,group)) {
            throw new DataIntegrityViolationException("Relation already exists between member and group");
        }
        ArtistRelationship artistRelationship = new ArtistRelationship(member, group);
        member.getGroups().add(artistRelationship);
        group.getMembers().add(artistRelationship);

        artistRelationshipRepository.save(artistRelationship);
    }

    @Transactional
    public void removeArtistFromGroup(Long memberId, Long groupId) {
        Artist member = artistRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);
        Artist group = artistRepository.findById(groupId).orElseThrow(EntityNotFoundException::new);

        ArtistRelationship artistRelationship = artistRelationshipRepository.findByMemberAndGroup(member, group)
                .orElseThrow(() -> new EntityNotFoundException("Relation does not exist between member and group"));
        member.getGroups().remove(artistRelationship);
        group.getMembers().remove(artistRelationship);
        artistRelationshipRepository.delete(artistRelationship);
    }
}
