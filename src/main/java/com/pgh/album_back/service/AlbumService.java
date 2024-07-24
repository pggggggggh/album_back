package com.pgh.album_back.service;

import com.pgh.album_back.dto.AlbumDTO;
import com.pgh.album_back.entity.Album;
import com.pgh.album_back.entity.AlbumArtist;
import com.pgh.album_back.entity.Artist;
import com.pgh.album_back.repository.AlbumArtistRepository;
import com.pgh.album_back.repository.AlbumRepository;
import com.pgh.album_back.repository.ArtistRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AlbumService {
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final AlbumArtistRepository albumArtistRepository;
    private final TrackService trackService;
//    private final ArtistService artistService;
    private final APIService apiService;

    @Transactional
    public String createAlbum(String id) {
        AlbumDTO albumDTO = apiService.fetchAlbum(id).blockOptional().orElseThrow(NoSuchElementException::new);
        Album album = new Album(id);

        album.setTitle(albumDTO.getTitle());
        album.setDisambiguation(albumDTO.getDisambiguation());
        album.setDate(albumDTO.getDate());
        album.setTypes(albumDTO.getTypes());

        album = albumRepository.save(album);

        for (var dtoArtist : albumDTO.getArtists()) {
            Artist artist = artistRepository.findById(dtoArtist.getId())
                    .orElseGet(() -> {
                        Artist newArtist = new Artist(dtoArtist.getId());
                        newArtist.setName(dtoArtist.getName());
                        return artistRepository.save(newArtist);
                    });

            AlbumArtist albumArtist = new AlbumArtist(album, artist);
            artist.addAlbum(albumArtist);
            album.addArtist(albumArtist);
            albumArtistRepository.save(albumArtist);
        }

        trackService.addTracksToAlbum(album, albumDTO.getAlbumTracks());
        return album.getId();
    }

    @Transactional
    public void addAlbumsOfArtist(String artistId, List<String> albumIds) {
        Artist artist = artistRepository.findById(artistId).orElseThrow(EntityNotFoundException::new);

        for (var albumId : albumIds) {
            AlbumDTO albumDTO = apiService.fetchAlbum(albumId).block();
            if (!albumRepository.existsById(albumId)) createAlbum(albumId);
        }
    }
}
