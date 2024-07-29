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
        Artist artist = artistRepository.findById(artistService.fetchAndCreateArtist("6b128dd7-e998-4edb-a4f5-400fef190445")).orElseThrow();

        return;
    }
}
