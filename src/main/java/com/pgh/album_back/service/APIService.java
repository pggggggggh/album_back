package com.pgh.album_back.service;

import com.pgh.album_back.dto.AlbumCreateDTO;
import com.pgh.album_back.dto.ArtistCreateDTO;
import reactor.core.publisher.Mono;

public interface APIService {

    public Mono<ArtistCreateDTO> fetchArtist(String id);

    public Mono<AlbumCreateDTO> fetchAlbum(String id);
}
