package com.pgh.album_back.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ArtistRelationship extends BaseEntity {
    @Id
    @Column(name = "artist_relationship_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDate beginDate;
    private LocalDate endDate;
    private boolean ended;

    private String type;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Artist member;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Artist group;

    public ArtistRelationship(Artist member, Artist group) {
        this.member = member;
        this.group = group;
    }
}
