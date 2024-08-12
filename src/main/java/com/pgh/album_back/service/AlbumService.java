package com.pgh.album_back.service;

import com.pgh.album_back.dto.AlbumCreateDTO;
import com.pgh.album_back.entity.Album;
import com.pgh.album_back.entity.AlbumArtist;
import com.pgh.album_back.entity.Artist;
import com.pgh.album_back.repository.AlbumArtistRepository;
import com.pgh.album_back.repository.AlbumRepository;
import com.pgh.album_back.repository.ArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbumService {
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final AlbumArtistRepository albumArtistRepository;
    private final TrackService trackService;
//    private final ArtistService artistService;
    private final APIService apiService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createAlbum(String id, boolean allowTrivial) {
        AlbumCreateDTO albumCreateDTO = apiService.fetchAlbum(id).blockOptional().orElseThrow(RuntimeException::new);
        Album album = albumRepository.findById(id).orElseGet(() -> new Album(id));

        if (!allowTrivial) {
            List<String> valid = Arrays.asList("Album", "Single", "EP", "Soundtrack", "Remix");
            for (String type : albumCreateDTO.getTypes()) {
                if (!valid.contains(type)) {
                    return;
                }
            }
        }

        album.setTitle(albumCreateDTO.getTitle());
        album.setDisambiguation(albumCreateDTO.getDisambiguation());
        album.setDate(albumCreateDTO.getDate());
        album.setTypes(albumCreateDTO.getTypes());
        album.setThumbUrlSmall(albumCreateDTO.getThumbUrlSmall());
        album.setThumbUrlMedium(albumCreateDTO.getThumbUrlMedium());
        album.setThumbUrlLarge(albumCreateDTO.getThumbUrlLarge());
        albumRepository.save(album);
        trackService.addTracksToAlbum(id, albumCreateDTO.getAlbumTracks());

        for (var dtoArtist : albumCreateDTO.getArtists()) {
            Artist artist = artistRepository.findById(dtoArtist.getId())
                    .orElseGet(() -> {
                        Artist newArtist = new Artist(dtoArtist.getId());
                        newArtist.setName(dtoArtist.getName());
                        return artistRepository.save(newArtist);
                    });

            if (albumArtistRepository.existsByAlbumAndArtist(album, artist)) continue;
            AlbumArtist albumArtist = new AlbumArtist(album, artist);
            artist.addAlbum(albumArtist);
            album.addArtist(albumArtist);
            albumArtistRepository.save(albumArtist);
        }
    }

    public Page<Album> getAlbums(Pageable pageable, String keyword, String sort) {
        if (sort.equalsIgnoreCase("random"))
            return albumRepository.findByKeywordOrderByRandom(keyword, pageable);

        if (sort.equalsIgnoreCase("most_reviewed"))
            return albumRepository.findByKeywordOrderByMostReviewed(keyword, pageable);

        return albumRepository.findByKeywordOrderByDateDesc(keyword, pageable);
    }
}
