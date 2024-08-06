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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Album extends Entry {
    @ElementCollection
    private List<String> types = new ArrayList<>();

    @OneToMany(mappedBy = "album")
    private Set<AlbumTrack> tracks = new HashSet<>();

    @Column(nullable = false)
    @OneToMany(mappedBy = "album")
    protected Set<AlbumArtist> artists = new HashSet<>();

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

    public AlbumSummaryDTO toAlbumSummaryDTO() {
        AlbumSummaryDTO albumSummaryDTO = new AlbumSummaryDTO();
        albumSummaryDTO.setId(id);
        albumSummaryDTO.setTitle(title);
        albumSummaryDTO.setDisambiguation(disambiguation);
        albumSummaryDTO.setDate(date);
        albumSummaryDTO.setReviewCount(getReviewCount());
        albumSummaryDTO.setAverageRating(getAverageRating());
        albumSummaryDTO.setThumbUrlMedium(thumbUrlMedium);
        albumSummaryDTO.setThumbUrlSmall(thumbUrlSmall);
        artists.forEach(albumArtist -> {
            albumSummaryDTO.addArtist(albumArtist.getArtist().getId(), albumArtist.getArtist().getName());
        });

        return albumSummaryDTO;
    }

    public AlbumDetailsDTO toAlbumDetailsDTO() {
        AlbumDetailsDTO albumDetailsDTO = new AlbumDetailsDTO();
        albumDetailsDTO.setId(id);
        albumDetailsDTO.setTitle(title);
        albumDetailsDTO.setDisambiguation(disambiguation);
        albumDetailsDTO.setDate(date);
        albumDetailsDTO.setReviewCount(getReviewCount());
        albumDetailsDTO.setAverageRating(getAverageRating());
        credits.forEach(credit -> {
            EntryDetailsDTO.Credit dtoCredit = new EntryDetailsDTO.Credit();
            dtoCredit.setTypes(credit.getTypes());
            dtoCredit.setArtistId(credit.getArtist().getId());
            dtoCredit.setArtistName(credit.getArtist().getName());

            albumDetailsDTO.addCredit(dtoCredit);
        });
        reviews.forEach(review -> {
            EntryDetailsDTO.Review dtoReview = new EntryDetailsDTO.Review();
            dtoReview.setUsername(review.getUser().getUsername());
            dtoReview.setUserNickname(review.getUser().getNickname());
            dtoReview.setRating(review.getRating());
            dtoReview.setTitle(review.getTitle());
            dtoReview.setContent(review.getContent());

            albumDetailsDTO.addReview(dtoReview);
        });

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
            albumDetailsDTO.addAlbumTrack(dtoAlbumTrack);
        });
        artists.forEach(albumArtist -> {
            EntryDetailsDTO.Artist dtoArtist = new EntryDetailsDTO.Artist();
            dtoArtist.setId(albumArtist.getArtist().getId());
            dtoArtist.setName(albumArtist.getArtist().getName());
            albumDetailsDTO.addArtist(dtoArtist);
        });

        return albumDetailsDTO;
    }
}