package com.pgh.album_back.controller;

import com.pgh.album_back.dto.AlbumSummaryDTO;
import com.pgh.album_back.entity.Album;
import com.pgh.album_back.repository.AlbumRepository;
import com.pgh.album_back.repository.TrackRepository;
import com.pgh.album_back.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EntryController {
    private final AlbumService albumService;
    private final AlbumRepository albumRepository;
    private final TrackRepository trackRepository;

    @GetMapping("/albums")
    public ResponseEntity<Page<AlbumSummaryDTO>> getAlbums(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Album> albumPage = albumService.getAlbums(pageable);
        Page<AlbumSummaryDTO> dtoPage = albumPage.map(Album::toAlbumSummaryDTO);
        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/{type}/details")
    public ResponseEntity<?> getEntryDetails(
            @PathVariable String type,
            @RequestParam String id) {
        if (type.equalsIgnoreCase("albums")) {
            return albumRepository.findById(id)
                    .map(album -> ResponseEntity.ok(album.toAlbumDetailsDTO()))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
        } else if (type.equalsIgnoreCase("tracks")) {
            return trackRepository.findById(id)
                    .map(track -> ResponseEntity.ok(track.toTrackDetailsDTO()))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid type: " + type);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("sdf");
    }
}
