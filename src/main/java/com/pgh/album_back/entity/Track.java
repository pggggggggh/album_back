package com.pgh.album_back.entity;

import com.pgh.album_back.dto.EntryDetailsDTO;
import com.pgh.album_back.dto.TrackDetailsDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Track extends Entry {
    @OneToMany(mappedBy = "track", orphanRemoval = true)
    private List<AlbumTrack> albums = new ArrayList<>();

    @Column(nullable = false)
    @OneToMany(mappedBy = "track", orphanRemoval = true)
    protected List<TrackArtist> artists = new ArrayList<>();

    protected Long length;

    public void addAlbum(AlbumTrack albumTrack) {
        albums.add(albumTrack);
    }

    public void addArtist(TrackArtist trackArtist) {
        artists.add(trackArtist);
    }

    public Track(String id) {
        super(id);
    }



    public TrackDetailsDTO toTrackDetailsDTO() {
        TrackDetailsDTO trackDetailsDTO = new TrackDetailsDTO();
        trackDetailsDTO.setId(id);
        trackDetailsDTO.setTitle(title);
        trackDetailsDTO.setDisambiguation(disambiguation);
        trackDetailsDTO.setDate(date);
        trackDetailsDTO.setReviewCount(getReviewCount());
        trackDetailsDTO.setAverageRating(getAverageRating());
        trackDetailsDTO.setGenres(getGenreNames());
        credits.forEach(credit -> {
            EntryDetailsDTO.Credit dtoCredit = new EntryDetailsDTO.Credit();
            dtoCredit.setTypes(credit.getTypes());
            dtoCredit.setArtistId(credit.getArtist().getId());
            dtoCredit.setArtistName(credit.getArtist().getName());

            trackDetailsDTO.addCredit(dtoCredit);
        });
        reviews.forEach(review -> {
            EntryDetailsDTO.Review dtoReview = new EntryDetailsDTO.Review();
            dtoReview.setUsername(review.getUser().getUsername());
            dtoReview.setUserNickname(review.getUser().getNickname());
            dtoReview.setRating(review.getRating());
            dtoReview.setTitle(review.getTitle());
            dtoReview.setContent(review.getContent());
            dtoReview.setCreatedAt(review.getCreatedAt());
            dtoReview.setUpdatedAt(review.getUpdatedAt());

            trackDetailsDTO.addReview(dtoReview);
        });
        artists.forEach(trackArtist -> {
            EntryDetailsDTO.Artist dtoArtist = new EntryDetailsDTO.Artist();
            dtoArtist.setId(trackArtist.getArtist().getId());
            dtoArtist.setName(trackArtist.getArtist().getName());
            trackDetailsDTO.addArtist(dtoArtist);
        });

        // 여기부터 트랙 전용
        List<Album> appearsAt = albums.stream().map(AlbumTrack::getAlbum)
                .sorted(Comparator.comparing(Album::getDate)).toList();
        Album primaryAlbum = appearsAt.get(0);
        trackDetailsDTO.setThumbUrlLarge(primaryAlbum.getThumbLarge().getImagePath());
        trackDetailsDTO.setThumbUrlMedium(primaryAlbum.getThumbMedium().getImagePath());
        trackDetailsDTO.setThumbUrlSmall(primaryAlbum.getThumbSmall().getImagePath());
        albums.forEach(albumTrack -> {
            TrackDetailsDTO.Album album = new TrackDetailsDTO.Album();
            album.setAlbumId(albumTrack.getAlbum().getId());
            album.setAlbumTitle(albumTrack.getAlbum().getTitle());
            album.setAlbumAverageRating(albumTrack.getAlbum().getTracksAverageRating());
            album.setAlbumReviewCount(albumTrack.getAlbum().getTracksReviewCount());
            album.setNumber(albumTrack.getNumber());

            trackDetailsDTO.addAppearsAt(album);
        });

        trackDetailsDTO.setLength(length);




        return trackDetailsDTO;
    }
}
