package com.pgh.album_back.service;

import com.pgh.album_back.dto.AlbumInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class MusicBrainzService implements APIService {
    private final WebClient webClient;
    public MusicBrainzService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://musicbrainz.org/ws/2").build();
    }

    public Mono<AlbumInfo> getAlbumInfo(String mbid) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/release/{mbid}")
                        .queryParam("inc", "recordings")
                        .queryParam("fmt", "json")
                        .build(mbid))
                .retrieve()
                .bodyToMono(AlbumInfo.class);
    }
}
