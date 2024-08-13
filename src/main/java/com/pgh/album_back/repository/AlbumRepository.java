package com.pgh.album_back.repository;

import com.pgh.album_back.entity.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AlbumRepository extends JpaRepository<Album, String> {
    @Query("SELECT DISTINCT album FROM Album album " +
            "JOIN album.tracks albumTrack " +
            "JOIN album.artists albumArtist " +
            "WHERE LOWER(albumTrack.track.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(album.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(albumArtist.artist.name) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
            "OR LOWER(albumArtist.artist.id) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
            "ORDER BY album.date DESC")
    Page<Album> findByKeywordOrderByDateDesc(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT album FROM Album album " +
            "JOIN album.tracks albumTrack " +
            "JOIN album.artists albumArtist " +
            "LEFT JOIN albumTrack.track.reviews review " +
            "WHERE LOWER(albumTrack.track.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(album.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(albumArtist.artist.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(albumArtist.artist.id) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
            "GROUP BY album " +
            "ORDER BY COUNT(review) DESC")
    Page<Album> findByKeywordOrderByMostReviewed(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT album FROM Album album " +
            "JOIN album.tracks albumTrack " +
            "JOIN album.artists albumArtist " +
            "LEFT JOIN albumTrack.track.reviews review " +
            "WHERE LOWER(albumTrack.track.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(album.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(albumArtist.artist.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(albumArtist.artist.id) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Album> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query(value = "SELECT DISTINCT album.*, NEWID() as rand_id FROM entry album " +
            "JOIN Album_Track albumTrack ON album.entry_id = albumTrack.album_id " +
            "JOIN entry track ON albumTrack.track_id = track.entry_id " +
            "JOIN Album_Artist albumArtist ON album.entry_id = albumArtist.album_id " +
            "JOIN Artist artist ON albumArtist.artist_id = artist.artist_id " +
            "WHERE LOWER(track.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(album.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(artist.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(artist.artist_id) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "ORDER BY rand_id",
            countQuery = "SELECT COUNT(DISTINCT album.entry_id) FROM entry album " +
                    "JOIN Album_Track albumTrack ON album.entry_id = albumTrack.album_id " +
                    "JOIN entry track ON albumTrack.track_id = track.entry_id " +
                    "JOIN Album_Artist albumArtist ON album.entry_id = albumArtist.album_id " +
                    "JOIN Artist artist ON albumArtist.artist_id = artist.artist_id " +
                    "WHERE LOWER(track.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
                    "OR LOWER(album.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
                    "OR LOWER(artist.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
                    "OR LOWER(artist.artist_id) LIKE LOWER(CONCAT('%', :keyword, '%')) ",
            nativeQuery = true)
    Page<Album> findByKeywordOrderByRandom(@Param("keyword") String keyword, Pageable pageable);
    boolean existsById(String id);
}
