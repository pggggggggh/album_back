package com.pgh.album_back.service;

import com.pgh.album_back.dto.AlbumDTO;
import com.pgh.album_back.dto.TrackDTO;
import com.pgh.album_back.entity.*;
import com.pgh.album_back.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrackService {
    private final TrackRepository trackRepository;
    private final AlbumRepository albumRepository;
    private final AlbumTrackRepository albumTrackRepository;
    private final TrackArtistRepository trackArtistRepository;
    private final CreditRepository creditRepository;
    private final ArtistRepository artistRepository;

    @Transactional
    public void addTracksToAlbum(Album album, List<AlbumDTO.AlbumTrack> albumTracks) {
        for (var dtoAlbumTrack : albumTracks) {
            TrackDTO trackDTO = dtoAlbumTrack.getTrack();
            Track track = trackRepository.findById(trackDTO.getId())
                    .orElseGet(() -> {
                        Track newTrack = new Track(trackDTO.getId());
                        newTrack.setTitle(trackDTO.getTitle());
                        newTrack.setDisambiguation(trackDTO.getDisambiguation());
                        newTrack.setDate(trackDTO.getFirstReleaseDate());
                        return trackRepository.save(newTrack);
                    });

            for (var dtoArtist : trackDTO.getArtists()) {
                Artist artist = artistRepository.findById(dtoArtist.getId())
                        .orElseGet(() -> {
                            Artist newArtist = new Artist(dtoArtist.getId());
                            newArtist.setName(dtoArtist.getName());
                            return artistRepository.save(newArtist);
                        });

                TrackArtist trackArtist = new TrackArtist(track, artist);
                track.addArtist(trackArtist);
                artist.addTrack(trackArtist);
                trackArtistRepository.save(trackArtist);
            }

            for (var entry : trackDTO.getCredits().entrySet()) {
                String artistId = entry.getKey();
                var dtoCredit = entry.getValue();
                Artist artist = artistRepository.findById(artistId)
                        .orElseGet(() -> {
                            Artist newArtist = new Artist(artistId);
                            newArtist.setName(dtoCredit.getArtistName());
                            return artistRepository.save(newArtist);
                        });

                Credit credit = new Credit(artist, track);
                credit.setTypes(dtoCredit.getTypes());
                artist.addCredit(credit);
                track.addCredit(credit);
                creditRepository.save(credit);
            }

            AlbumTrack albumTrack = new AlbumTrack(album, track);
            albumTrack.setNumber(dtoAlbumTrack.getNumber());
            albumTrack.setPosition(dtoAlbumTrack.getPosition());
            album.addTrack(albumTrack);
            track.addAlbum(albumTrack);
            albumTrackRepository.save(albumTrack);
        }
    }
}
