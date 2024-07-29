package com.pgh.album_back.entity;

import com.pgh.album_back.dto.AlbumCreateDTO;
import com.pgh.album_back.dto.ArtistCreateDTO;
import com.pgh.album_back.service.MusicBrainzService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@SpringBootTest
@ActiveProfiles("local")
public class MusicBrainzServiceTest {
    @Autowired
    MusicBrainzService musicBrainzService;

    @Test
    public void getAlbumInfoTest() {
        AlbumCreateDTO musicBrainzAlbumCreateDTO = musicBrainzService.fetchAlbum("ae268385-3eb9-45ea-8221-b5269750c8b0").block();

        return;
    }

    @Test
    public void getArtistInfoTest() {
        ArtistCreateDTO artistCreateDTO = musicBrainzService.fetchArtist("b51c672b-85e0-48fe-8648-470a2422229f").block();

        return;
    }
}
