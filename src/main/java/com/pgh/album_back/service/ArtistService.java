package com.pgh.album_back.service;

import com.pgh.album_back.dto.ArtistDTO;
import com.pgh.album_back.entity.Artist;
import com.pgh.album_back.entity.ArtistRelationship;
import com.pgh.album_back.repository.ArtistRelationshipRepository;
import com.pgh.album_back.repository.ArtistRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ArtistService {
    private final ArtistRepository artistRepository;
    private final ArtistRelationshipRepository artistRelationshipRepository;
    private final AlbumService albumService;
    private final APIService apiService;

    @Transactional
    public void saveArtist(Artist artist) {
        artistRepository.save(artist);
    }

    @Transactional
    public String createArtist(String id, String name) {
        Artist artist = new Artist(id);
        artist.setName(name);
        artist = artistRepository.save(artist);

        return artist.getId();
    }

    @Transactional
    public String fetchAndCreateArtist(String id) {
        ArtistDTO artistDTO = apiService.fetchArtist(id).blockOptional().orElseThrow(NoSuchElementException::new);
        Artist artist = new Artist(id);

        artist.setName(artistDTO.getName());
        artist.setDisambiguation(artistDTO.getDisambiguation());
        artist.setType(artistDTO.getType());
        artist.setGender(artistDTO.getGender());
        artist.setCountry(artistDTO.getCountry());
        artist.setArea(artistDTO.getArea());
        artist.setBeginArea(artistDTO.getBeginArea());
        artist.setEndArea(artistDTO.getEndArea());
        artist.setBeginDate(artistDTO.getBeginDate());
        artist.setEndDate(artistDTO.getEndDate());
        artistRepository.save(artist);
        albumService.addAlbumsOfArtist(id, artistDTO.getAlbumIds());

        return artist.getId();
    }

    @Transactional
    public void addArtistToGroup(String memberId, String groupId) {
        Artist member = artistRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);
        Artist group = artistRepository.findById(groupId).orElseThrow(EntityNotFoundException::new);

        if (artistRelationshipRepository.existsByMemberAndGroup(member,group)) {
            throw new DataIntegrityViolationException("Relation already exists between member and group");
        }
        ArtistRelationship artistRelationship = new ArtistRelationship(member, group);
        member.addGroup(artistRelationship);
        group.addMember(artistRelationship);
        artistRelationshipRepository.save(artistRelationship);
    }

    @Transactional
    public void removeArtistFromGroup(String memberId, String groupId) {
        Artist member = artistRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);
        Artist group = artistRepository.findById(groupId).orElseThrow(EntityNotFoundException::new);

        ArtistRelationship artistRelationship = artistRelationshipRepository.findByMemberAndGroup(member, group)
                .orElseThrow(() -> new EntityNotFoundException("Relation does not exist between member and group"));
        member.removeGroup(artistRelationship);
        group.removeMember(artistRelationship);
        artistRelationshipRepository.delete(artistRelationship);
    }
}
