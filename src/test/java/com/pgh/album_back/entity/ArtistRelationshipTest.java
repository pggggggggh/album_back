package com.pgh.album_back.entity;

import com.pgh.album_back.repository.ArtistRepository;
import com.pgh.album_back.service.ArtistService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("local")
public class ArtistRelationshipTest {
    @Autowired
    ArtistRepository artistRepository;
    @Autowired
    ArtistService artistService;
    @PersistenceContext
    EntityManager em;

    @Test
    @Transactional
    public void multipleGroupTest() {
        Artist karina = Artist.createArtist("Karina");
        Artist aespa = Artist.createArtist("aespa");
        Artist gotTheBeat = Artist.createArtist("GOT the beat");

        artistService.saveArtist(karina);
        artistService.saveArtist(aespa);
        artistService.saveArtist(gotTheBeat);

        artistService.addArtistToGroup(karina.getId(), aespa.getId());
        artistService.addArtistToGroup(karina.getId(), gotTheBeat.getId());

        em.flush();
        em.clear();

        Artist newKarina = artistRepository.findById(karina.getId()).orElseThrow();
        Artist newAespa = artistRepository.findById(aespa.getId()).orElseThrow();
        Artist newGotTheBeat = artistRepository.findById(gotTheBeat.getId()).orElseThrow();

        assertEquals(2, newKarina.getGroups().size());
        assertTrue(newAespa.getMembers().stream().anyMatch(ag -> ag.getMember().getId().equals(newKarina.getId())));
        assertTrue(newGotTheBeat.getMembers().stream().anyMatch(ag -> ag.getMember().getId().equals(newKarina.getId())));
    }
}
