package com.pgh.album_back.entity;

import com.pgh.album_back.service.APIService;
import com.pgh.album_back.service.AlbumService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@SpringBootTest
@ActiveProfiles("local")
public class AlbumServiceTest {
    @Autowired APIService apiService;
    @Autowired AlbumService albumService;

    @Test
    public void createAlbumTest() {
        String savageId = "d35ed94d-8134-40b3-989a-6d1c5af9657b";
        albumService.createAlbum(savageId);

      }
}
