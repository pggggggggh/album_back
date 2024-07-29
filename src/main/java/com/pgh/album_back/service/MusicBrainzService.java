package com.pgh.album_back.service;

import com.pgh.album_back.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class MusicBrainzService implements APIService {
    public static final long REQUEST_DELAY = 5000;
    public static final long MAX_ATTEMPTS = 3;
    private final WebClient webClient;
    public MusicBrainzService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://musicbrainz.org/ws/2").build();
    }

    @Override
    public Mono<ArtistCreateDTO> fetchArtist(String id) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/artist/{id}")
                        .queryParam("inc", "release-groups+artist-rels")
                        .queryParam("fmt", "json")
                        .build(id))
                .retrieve()
                .bodyToMono(MusicBrainzArtistDTO.class)
                .retryWhen(Retry.fixedDelay(MAX_ATTEMPTS, Duration.ofMillis(REQUEST_DELAY)))
                .map(musicBrainzArtistDTO -> {
                    ArtistCreateDTO artistCreateDTO = new ArtistCreateDTO();

                    artistCreateDTO.setId(musicBrainzArtistDTO.getId());
                    artistCreateDTO.setName(musicBrainzArtistDTO.getName());
                    artistCreateDTO.setDisambiguation(musicBrainzArtistDTO.getDisambiguation());
                    artistCreateDTO.setType(musicBrainzArtistDTO.getType());
                    artistCreateDTO.setGender(musicBrainzArtistDTO.getGender());
                    artistCreateDTO.setArea(musicBrainzArtistDTO.getArea().getName());
                    artistCreateDTO.setBeginArea(musicBrainzArtistDTO.getBeginArea().getName());

                    for (var releaseGroup : musicBrainzArtistDTO.getReleaseGroups()) {
                        artistCreateDTO.addAlbumId(releaseGroup.getId());
                    }

                    for (var dtoRelation : musicBrainzArtistDTO.getRelations()) {
                        ArtistCreateDTO.Relation relation = new ArtistCreateDTO.Relation();
                        relation.setArtistId(dtoRelation.getArtist().getId());
                        relation.setType(dtoRelation.getType());

                        artistCreateDTO.addRelation(relation);
                    }

                    return artistCreateDTO;
                });
    }

    @Override
    public Mono<AlbumCreateDTO> fetchAlbum(String id) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/release-group/{id}")
                        .queryParam("inc", "releases+media")
                        .queryParam("fmt", "json")
                        .build(id))
                .retrieve()
                .bodyToMono(MusicBrainzReleaseGroupDTO.class)
                .retryWhen(Retry.fixedDelay(MAX_ATTEMPTS, Duration.ofMillis(REQUEST_DELAY)))
                .flatMap(musicBrainzReleaseGroupDTO -> {
                    String releaseId = musicBrainzReleaseGroupDTO.getReleases().get(0).getId();

                    return webClient.get()
                            .uri(uriBuilder -> uriBuilder
                                    .path("/release/{releaseId}")
                                    .queryParam("inc", "recordings+release-groups+artists+work-rels+work-level-rels+artist-rels+recording-level-rels+artist-credits")
                                    .queryParam("fmt", "json")
                                    .build(releaseId))
                            .retrieve()
                            .bodyToMono(MusicBrainzReleaseDTO.class)
                            .retryWhen(Retry.fixedDelay(MAX_ATTEMPTS, Duration.ofMillis(REQUEST_DELAY)))
                            .map(
                                    musicBrainzReleaseDTO -> {
                                        AlbumCreateDTO albumCreateDTO = new AlbumCreateDTO();
                                        albumCreateDTO.setId(musicBrainzReleaseDTO.getId());
                                        albumCreateDTO.setTitle(musicBrainzReleaseDTO.getTitle());
                                        albumCreateDTO.addType(musicBrainzReleaseGroupDTO.getPrimaryType());
                                        for (var type : musicBrainzReleaseGroupDTO.getSecondaryTypes()) {
                                            albumCreateDTO.addType(type);
                                        }
                                        albumCreateDTO.setDisambiguation(musicBrainzReleaseDTO.getDisambiguation());
                                        albumCreateDTO.setDate(musicBrainzReleaseDTO.getDate());

                                        for (var artistCredit : musicBrainzReleaseDTO.getArtistCredits()) {
                                            AlbumCreateDTO.Artist artist = new AlbumCreateDTO.Artist();
                                            artist.setId(artistCredit.getArtist().getId());
                                            artist.setName(artistCredit.getArtist().getName());
                                            albumCreateDTO.addArtist(artist);
                                        }

                                        for (var media : musicBrainzReleaseDTO.getMedias()) {
                                            for (var dtoTrack : media.getTracks()) {
                                                TrackCreateDTO track = new TrackCreateDTO();
                                                var recording = dtoTrack.getRecording();

                                                track.setId(recording.getId());
                                                track.setTitle(recording.getTitle());
                                                track.setDisambiguation(recording.getDisambiguation());
                                                track.setLength(recording.getLength());
                                                track.setFirstReleaseDate(recording.getFirstReleaseDate());

                                                for (var artistCredit : dtoTrack.getArtistCredits()) {
                                                    TrackCreateDTO.Artist artist = new TrackCreateDTO.Artist();
                                                    artist.setId(artistCredit.getArtist().getId());
                                                    artist.setName(artistCredit.getArtist().getName());
                                                    track.addArtist(artist);
                                                }

                                                List<MusicBrainzReleaseDTO.Recording.Relation> relationsToAdd = new ArrayList<>();
                                                for (var relation : recording.getRelations()) {
                                                    if (relation.getWork() != null) {
                                                        relationsToAdd.addAll(relation.getWork().getRelations());
                                                    } else {
                                                        relationsToAdd.add(relation);
                                                    }
                                                }
                                                for (var relation : relationsToAdd) {
                                                    if (!relation.getTargetType().equals("artist")) continue;

                                                    TrackCreateDTO.Credit credit = new TrackCreateDTO.Credit();
                                                    credit.setArtistName(relation.getArtist().getName());
                                                    if (!relation.getAttributes().isEmpty()) {
                                                        relation.getAttributes().forEach(
                                                                credit::addType
                                                        );
                                                    } else {
                                                        credit.addType(relation.getType());
                                                    }

                                                    track.addCredit(relation.getArtist().getId(), credit);
                                                }

                                                AlbumCreateDTO.AlbumTrack albumTrack = new AlbumCreateDTO.AlbumTrack();
                                                albumTrack.setNumber(dtoTrack.getNumber());
                                                albumTrack.setPosition(dtoTrack.getPosition());
                                                albumTrack.setTrack(track);
                                                albumCreateDTO.addTrack(albumTrack);
                                            }
                                        }

                                        return albumCreateDTO;
                                    }
                            );
                });

    }
}