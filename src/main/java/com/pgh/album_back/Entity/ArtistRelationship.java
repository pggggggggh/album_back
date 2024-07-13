package com.pgh.album_back.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ArtistRelationship {
    @Id
    @Column(name = "artist_relationship_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String role;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Artist group;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Artist member;

    public ArtistRelationship(Artist group, Artist member) {
        this.group = group;
        this.member = member;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArtistRelationship that = (ArtistRelationship) o;
        return Objects.equals(group, that.group) && Objects.equals(member, that.member);
    }

    @Override
    public int hashCode() {
        return Objects.hash(group, member);
    }
}
