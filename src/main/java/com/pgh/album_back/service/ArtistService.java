package com.pgh.album_back.service;

import com.pgh.album_back.dto.ArtistCreateDTO;
import com.pgh.album_back.entity.Artist;
import com.pgh.album_back.entity.ArtistRelationship;
import com.pgh.album_back.repository.ArtistRelationshipRepository;
import com.pgh.album_back.repository.ArtistRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArtistService {
    private final ArtistRepository artistRepository;
    private final ArtistRelationshipRepository artistRelationshipRepository;
    private final AlbumService albumService;
    private final APIService apiService;
    private final PlatformTransactionManager transactionManager;

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

    @Async
    @Transactional
    public void fetchAndCreateArtist(String id) {
        // block 안하면 Transactional 붙은 함수가 subscribe 남겨놓고 리턴하면 세션이 끝나버려서 LazyException 뜸
        ArtistCreateDTO artistCreateDTO = apiService.fetchArtist(id).blockOptional().orElseThrow();
        Artist artist = artistRepository.findById(artistCreateDTO.getId())
                        .orElseGet(() -> new Artist(artistCreateDTO.getId()));

        artist.setName(artistCreateDTO.getName());
        artist.setDisambiguation(artistCreateDTO.getDisambiguation());
        artist.setType(artistCreateDTO.getType());
        artist.setGender(artistCreateDTO.getGender());
        artist.setCountry(artistCreateDTO.getCountry());
        artist.setArea(artistCreateDTO.getArea());
        artist.setBeginArea(artistCreateDTO.getBeginArea());
        artist.setEndArea(artistCreateDTO.getEndArea());
        artist.setBeginDate(artistCreateDTO.getBeginDate());
        artist.setEndDate(artistCreateDTO.getEndDate());
        artistRepository.save(artist);

        artistCreateDTO.getAlbumIds().forEach((albumId) -> {
            albumService.createAlbum(albumId, false);
        });
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
