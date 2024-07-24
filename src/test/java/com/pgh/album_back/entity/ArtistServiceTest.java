package com.pgh.album_back.entity;

import com.pgh.album_back.repository.ArtistRepository;
import com.pgh.album_back.service.ArtistService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@SpringBootTest
@ActiveProfiles("local")
public class ArtistServiceTest {
    @Autowired
    ArtistService artistService;
    @Autowired
    ArtistRepository artistRepository;

    @Test
    @Transactional
    public void createArtistTest() {
        Artist artist = artistRepository.findById(artistService.fetchAndCreateArtist("b51c672b-85e0-48fe-8648-470a2422229f")).orElseThrow();

        return;
    }
}
