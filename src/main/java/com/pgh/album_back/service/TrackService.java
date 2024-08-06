package com.pgh.album_back.service;

import com.pgh.album_back.dto.AlbumCreateDTO;
import com.pgh.album_back.dto.TrackCreateDTO;
import com.pgh.album_back.entity.*;
import com.pgh.album_back.repository.*;
import jakarta.persistence.EntityNotFoundException;
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
    public void addTracksToAlbum(String albumId, List<AlbumCreateDTO.AlbumTrack> albumTracks) {
        for (var dtoAlbumTrack : albumTracks) {
            TrackCreateDTO trackCreateDTO = dtoAlbumTrack.getTrack();
            Track track = trackRepository.findById(trackCreateDTO.getId())
                    .orElseGet(() -> {
                        Track newTrack = new Track(trackCreateDTO.getId());
                        newTrack.setTitle(trackCreateDTO.getTitle());
                        newTrack.setDisambiguation(trackCreateDTO.getDisambiguation());
                        newTrack.setLength(trackCreateDTO.getLength());
                        newTrack.setDate(trackCreateDTO.getFirstReleaseDate());
                        return trackRepository.save(newTrack);
                    });

            for (var dtoArtist : trackCreateDTO.getArtists()) {
                Artist artist = artistRepository.findById(dtoArtist.getId())
                        .orElseGet(() -> {
                            Artist newArtist = new Artist(dtoArtist.getId());
                            newArtist.setName(dtoArtist.getName());
                            return artistRepository.save(newArtist);
                        });

                if (trackArtistRepository.existsByTrackAndArtist(track,artist)) continue;
                TrackArtist trackArtist = new TrackArtist(track, artist);
                track.addArtist(trackArtist);
                artist.addTrack(trackArtist);
                trackArtistRepository.save(trackArtist);
            }

            for (var entry : trackCreateDTO.getCredits().entrySet()) {
                String artistId = entry.getKey();
                var dtoCredit = entry.getValue();
                Artist artist = artistRepository.findById(artistId)
                        .orElseGet(() -> {
                            Artist newArtist = new Artist(artistId);
                            newArtist.setName(dtoCredit.getArtistName());
                            return artistRepository.save(newArtist);
                        });

                if (creditRepository.existsByArtistAndEntry(artist, track)) continue;
                Credit credit = new Credit(artist, track);
                credit.setTypes(dtoCredit.getTypes());
                artist.addCredit(credit);
                track.addCredit(credit);
                creditRepository.save(credit);
            }

            Album album = albumRepository.findById(albumId).orElseThrow(EntityNotFoundException::new);

            if (albumTrackRepository.existsByAlbumAndTrack(album,track)) continue;
            AlbumTrack albumTrack = new AlbumTrack(album, track);
            albumTrack.setNumber(dtoAlbumTrack.getNumber());
            albumTrack.setPosition(dtoAlbumTrack.getPosition());
            album.addTrack(albumTrack);
            track.addAlbum(albumTrack);
            albumTrackRepository.save(albumTrack);
        }
    }
}
