package com.pgh.album_back.service;

import com.pgh.album_back.dto.AlbumDTO;
import com.pgh.album_back.dto.ArtistDTO;
import reactor.core.publisher.Mono;

public interface APIService {

    public Mono<ArtistDTO> fetchArtist(String id);

    public Mono<AlbumDTO> fetchAlbum(String id);
}
