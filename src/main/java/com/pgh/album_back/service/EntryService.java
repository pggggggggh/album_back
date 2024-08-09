package com.pgh.album_back.service;

import com.pgh.album_back.entity.Entry;
import com.pgh.album_back.entity.Genre;
import com.pgh.album_back.entity.GenreVote;
import com.pgh.album_back.entity.User;
import com.pgh.album_back.repository.EntryRepository;
import com.pgh.album_back.repository.GenreRepository;
import com.pgh.album_back.repository.GenreVoteRepository;
import com.pgh.album_back.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EntryService {
    private final EntryRepository entryRepository;
    private final GenreRepository genreRepository;
    private final UserRepository userRepository;
    private final GenreVoteRepository genreVoteRepository;

    @Transactional
    public void addVote(String entryId, String username, List<String> genreNames) {
        Entry entry = entryRepository.findById(entryId)
                .orElseThrow(() -> new EntityNotFoundException("Entry not found"));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        List<Genre> genres = genreNames.stream()
                .map((genreName) -> genreRepository.findByNameIgnoreCase(genreName)
                        .orElseThrow(() -> new EntityNotFoundException("Genre not found"))).toList();
        genreVoteRepository.deleteAllByEntryAndUser(entry, user); // 원래 투표가 있다면 삭제

        genres.forEach((genre) -> {
            GenreVote genreVote = new GenreVote();
            genreVote.setGenre(genre);
            user.addGenreVote(genreVote);
            entry.addGenreVote(genreVote);
            genreVote.setVotes(1);

            genreVoteRepository.save(genreVote);
        });
    }

    @Transactional
    public List<String> getMyVotes(String entryId, String username) {
        Entry entry = entryRepository.findById(entryId)
                .orElseThrow(() -> new EntityNotFoundException("Entry not found"));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return genreVoteRepository.findByEntryAndUser(entry, user)
                .stream().map(genreVote -> genreVote.getGenre().getName()).toList();
    }
}
