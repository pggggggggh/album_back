package com.pgh.album_back.repository;

import com.pgh.album_back.entity.Entry;
import com.pgh.album_back.entity.Genre;
import com.pgh.album_back.entity.GenreVote;
import com.pgh.album_back.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GenreVoteRepository extends JpaRepository<GenreVote, Long> {
    List<GenreVote> findByEntryAndGenre(Entry entry, Genre genre);
    void deleteAllByEntryAndUser(Entry entry, User user);
    List<GenreVote> findByEntryAndUser(Entry entry, User user);
}
