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
            "ORDER BY album.date DESC")
    Page<Album> findByKeywordOrderByDateDesc(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT album FROM Album album " +
            "JOIN album.tracks albumTrack " +
            "JOIN album.artists albumArtist " +
            "LEFT JOIN albumTrack.track.reviews review " +
            "WHERE LOWER(albumTrack.track.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(album.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(albumArtist.artist.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "GROUP BY album " +
            "ORDER BY COUNT(review) DESC")
    Page<Album> findByKeywordOrderByMostReviewed(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT album FROM Album album " +
            "JOIN album.tracks albumTrack " +
            "JOIN album.artists albumArtist " +
            "LEFT JOIN albumTrack.track.reviews review " +
            "WHERE LOWER(albumTrack.track.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(album.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(albumArtist.artist.name) LIKE LOWER(CONCAT('%', :keyword, '%')) ")
    Page<Album> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT DISTINCT album FROM Album album " +
            "JOIN album.tracks albumTrack " +
            "JOIN album.artists albumArtist " +
            "LEFT JOIN albumTrack.track.reviews review " +
            "WHERE LOWER(albumTrack.track.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(album.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(albumArtist.artist.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "ORDER BY FUNCTION('RAND') ")
    Page<Album> findByKeywordOrderByRandom(@Param("keyword") String keyword, Pageable pageable);

    boolean existsById(String id);
}
