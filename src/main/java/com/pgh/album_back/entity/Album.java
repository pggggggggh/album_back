package com.pgh.album_back.entity;

import com.pgh.album_back.dto.AlbumDetailsDTO;
import com.pgh.album_back.dto.AlbumSummaryDTO;
import com.pgh.album_back.dto.EntryDetailsDTO;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Album extends Entry {
    @ElementCollection
    private List<String> types = new ArrayList<>();

    @OneToMany(mappedBy = "album", orphanRemoval = true)
    private Set<AlbumTrack> tracks = new HashSet<>();

    @Column(nullable = false)
    @OneToMany(mappedBy = "album", orphanRemoval = true)
    protected Set<AlbumArtist> artists = new HashSet<>();

    @ColumnDefault("0")
    private Long tracksReviewCount;

    @ColumnDefault("0")
    private Double tracksAverageRating;

    private String thumbUrlSmall; // 250
    private String thumbUrlMedium; // 500
    private String thumbUrlLarge; // 1200

    public Album(String id) {
        super(id);
    }

    public void addTrack(AlbumTrack albumTrack) {
        tracks.add(albumTrack);
    }

    public void addArtist(AlbumArtist albumArtist) {
        artists.add(albumArtist);
    }

    private void update() {
        tracksReviewCount = getValidTracks()
                .stream().mapToLong(Track::getReviewCount).sum();
        tracksAverageRating = getValidTracks()
                .stream().filter(track -> !track.getReviewCount().equals(0L))
                .mapToDouble(Track::getAverageRating)
                .average()
                .orElse(0.0);
    }

    @Override
    public void addReview(Review review) {
        reviews.add(review);
        review.setEntry(this);
        update();
    }

    @Override
    public void addGenreVote(GenreVote genreVote) {
        genreVotes.add(genreVote);
        genreVote.setEntry(this);
        update();
    }

    public AlbumSummaryDTO toAlbumSummaryDTO() {
        AlbumSummaryDTO albumSummaryDTO = new AlbumSummaryDTO();
        albumSummaryDTO.setId(id);
        albumSummaryDTO.setTitle(title);
        albumSummaryDTO.setDisambiguation(disambiguation);
        albumSummaryDTO.setDate(date);
//        albumSummaryDTO.setReviewCount(getReviewCount());
//        albumSummaryDTO.setAverageRating(getAverageRating());
        albumSummaryDTO.setReviewCount(tracksReviewCount);
        albumSummaryDTO.setAverageRating(tracksAverageRating);
        albumSummaryDTO.setThumbUrlMedium(thumbUrlMedium);
        albumSummaryDTO.setThumbUrlSmall(thumbUrlSmall);
        artists.forEach(albumArtist -> {
            albumSummaryDTO.addArtist(albumArtist.getArtist().getId(), albumArtist.getArtist().getName());
        });

        return albumSummaryDTO;
    }

    public List<Track> getValidTracks() {
        return tracks.stream().map(AlbumTrack::getTrack)
                .filter(track -> !track.getGenreNames().containsKey("Instrumental/Skit")).toList();
    }

    @Override
    public Map<String, Integer> getGenreNames() { // 여기서 value는 몇 개의 트랙에서 등장하는지
        List<Set<String>> trackGenres = getValidTracks()
                .stream().map((track) -> track.getGenreNames().keySet())
                .toList();

        Map<String, Integer> albumGenre = new HashMap<>();
        for (var trackGenre : trackGenres) {
            for (String genre : trackGenre) {
                albumGenre.put(genre, albumGenre.getOrDefault(genre, 0) + 1);
            }
        }

        return albumGenre.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
    }

    public AlbumDetailsDTO toAlbumDetailsDTO() {
        AlbumDetailsDTO albumDetailsDTO = new AlbumDetailsDTO();
        albumDetailsDTO.setId(id);
        albumDetailsDTO.setTitle(title);
        albumDetailsDTO.setDisambiguation(disambiguation);
        albumDetailsDTO.setDate(date);
        albumDetailsDTO.setReviewCount(tracksReviewCount);
        albumDetailsDTO.setAverageRating(tracksAverageRating);
        albumDetailsDTO.setGenres(getGenreNames());
        credits.forEach(credit -> {
            EntryDetailsDTO.Credit dtoCredit = new EntryDetailsDTO.Credit();
            dtoCredit.setTypes(credit.getTypes());
            dtoCredit.setArtistId(credit.getArtist().getId());
            dtoCredit.setArtistName(credit.getArtist().getName());

            albumDetailsDTO.addCredit(dtoCredit);
        });
//        reviews.forEach(review -> {
//            EntryDetailsDTO.Review dtoReview = new EntryDetailsDTO.Review();
//            dtoReview.setUsername(review.getUser().getUsername());
//            dtoReview.setUserNickname(review.getUser().getNickname());
//            dtoReview.setRating(review.getRating());
//            dtoReview.setTitle(review.getTitle());
//            dtoReview.setContent(review.getContent());
//            dtoReview.setCreatedAt(review.getCreatedAt());
//            dtoReview.setUpdatedAt(review.getUpdatedAt());
//
//            albumDetailsDTO.addReview(dtoReview);
//        });
        tracks.stream().map(AlbumTrack::getTrack).map(Track::getReviews)
                .flatMap(List::stream)
                .filter(review -> !review.getTitle().isEmpty())
                .forEach(review -> {
                    EntryDetailsDTO.Review dtoReview = new EntryDetailsDTO.Review();
                    dtoReview.setUsername(review.getUser().getUsername());
                    dtoReview.setUserNickname(review.getUser().getNickname());
                    dtoReview.setRating(review.getRating());
                    dtoReview.setTitle(review.getTitle());
                    dtoReview.setContent(review.getContent());
                    dtoReview.setCreatedAt(review.getCreatedAt());
                    dtoReview.setUpdatedAt(review.getUpdatedAt());
                    dtoReview.setTrackId(review.getEntry().getId());
                    dtoReview.setTrackTitle(review.getEntry().getTitle());

                    tracks.stream()
                            .filter(albumTrack -> Objects.equals(albumTrack.getTrack().getId(), review.getEntry().getId()))
                            .findFirst().ifPresent(albumTrack -> dtoReview.setTrackNumber(albumTrack.getNumber()));

                    albumDetailsDTO.addReview(dtoReview);
                });
        artists.forEach(albumArtist -> {
            EntryDetailsDTO.Artist dtoArtist = new EntryDetailsDTO.Artist();
            dtoArtist.setId(albumArtist.getArtist().getId());
            dtoArtist.setName(albumArtist.getArtist().getName());
            albumDetailsDTO.addArtist(dtoArtist);
        });

        // 여기부터 앨범 전용
        albumDetailsDTO.setThumbUrlLarge(thumbUrlLarge);
        albumDetailsDTO.setThumbUrlMedium(thumbUrlMedium);
        albumDetailsDTO.setThumbUrlSmall(thumbUrlSmall);
        albumDetailsDTO.setTypes(types);
        tracks.forEach(albumTrack -> {
            AlbumDetailsDTO.AlbumTrack dtoAlbumTrack = new AlbumDetailsDTO.AlbumTrack();
            Track track = albumTrack.getTrack();
            dtoAlbumTrack.setId(track.getId());
            dtoAlbumTrack.setNumber(albumTrack.getNumber());
            dtoAlbumTrack.setPosition(albumTrack.getPosition());
            dtoAlbumTrack.setTitle(track.getTitle());
            dtoAlbumTrack.setLength(track.getLength());
            dtoAlbumTrack.setAverageRating(track.getAverageRating());
            dtoAlbumTrack.setReviewCount(track.getReviewCount());
            albumDetailsDTO.addAlbumTrack(dtoAlbumTrack);
        });


        return albumDetailsDTO;
    }
}