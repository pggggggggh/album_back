package com.pgh.album_back.entity;

import com.pgh.album_back.dto.AlbumInfo;
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
        AlbumInfo albumInfo = musicBrainzService.getAlbumInfo("34bb5a33-2f77-40fe-9e40-fb434b49fd1d").block();
        return;
    }
}
