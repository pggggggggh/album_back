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
    public Mono<ArtistDTO> fetchArtist(String id) {
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
                    ArtistDTO artistDTO = new ArtistDTO();

                    artistDTO.setId(musicBrainzArtistDTO.getId());
                    artistDTO.setName(musicBrainzArtistDTO.getName());
                    artistDTO.setDisambiguation(musicBrainzArtistDTO.getDisambiguation());
                    artistDTO.setType(musicBrainzArtistDTO.getType());
                    artistDTO.setGender(musicBrainzArtistDTO.getGender());
                    artistDTO.setArea(musicBrainzArtistDTO.getArea().getName());
                    artistDTO.setBeginArea(musicBrainzArtistDTO.getBeginArea().getName());

                    for (var releaseGroup : musicBrainzArtistDTO.getReleaseGroups()) {
                        artistDTO.addAlbumId(releaseGroup.getId());
                    }

                    for (var dtoRelation : musicBrainzArtistDTO.getRelations()) {
                        ArtistDTO.Relation relation = new ArtistDTO.Relation();
                        relation.setArtistId(dtoRelation.getArtist().getId());
                        relation.setType(dtoRelation.getType());

                        artistDTO.addRelation(relation);
                    }

                    return artistDTO;
                });
    }

    @Override
    public Mono<AlbumDTO> fetchAlbum(String id) {
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
                                        AlbumDTO albumDTO = new AlbumDTO();
                                        albumDTO.setId(musicBrainzReleaseDTO.getId());
                                        albumDTO.setTitle(musicBrainzReleaseDTO.getTitle());
                                        albumDTO.addType(musicBrainzReleaseGroupDTO.getPrimaryType());
                                        for (var type : musicBrainzReleaseGroupDTO.getSecondaryTypes()) {
                                            albumDTO.addType(type);
                                        }
                                        albumDTO.setDisambiguation(musicBrainzReleaseDTO.getDisambiguation());
                                        albumDTO.setDate(musicBrainzReleaseDTO.getDate());

                                        for (var artistCredit : musicBrainzReleaseDTO.getArtistCredits()) {
                                            AlbumDTO.Artist artist = new AlbumDTO.Artist();
                                            artist.setId(artistCredit.getArtist().getId());
                                            artist.setName(artistCredit.getArtist().getName());
                                            albumDTO.addArtist(artist);
                                        }

                                        for (var media : musicBrainzReleaseDTO.getMedias()) {
                                            for (var dtoTrack : media.getTracks()) {
                                                TrackDTO track = new TrackDTO();
                                                var recording = dtoTrack.getRecording();

                                                track.setId(recording.getId());
                                                track.setTitle(recording.getTitle());
                                                track.setDisambiguation(recording.getDisambiguation());
                                                track.setLength(recording.getLength());
                                                track.setFirstReleaseDate(recording.getFirstReleaseDate());

                                                for (var artistCredit : dtoTrack.getArtistCredits()) {
                                                    TrackDTO.Artist artist = new TrackDTO.Artist();
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

                                                    TrackDTO.Credit credit = new TrackDTO.Credit();
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

                                                AlbumDTO.AlbumTrack albumTrack = new AlbumDTO.AlbumTrack();
                                                albumTrack.setNumber(dtoTrack.getNumber());
                                                albumTrack.setPosition(dtoTrack.getPosition());
                                                albumTrack.setTrack(track);
                                                albumDTO.addTrack(albumTrack);
                                            }
                                        }

                                        return albumDTO;
                                    }
                            );
                });

    }
}